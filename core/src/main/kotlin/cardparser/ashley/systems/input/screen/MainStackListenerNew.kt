package cardparser.ashley.systems.input.screen

import cardparser.ashley.systems.input.screen.TouchInputStatus.TOUCH
import cardparser.entities.MainStack
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import cardparser.tasks.CardPosition
import cardparser.tasks.GameActionHistory
import cardparser.tasks.TaskManager
import cardparser.tasks.cancel.MainStackTouchCancel

class MainStackListenerNew(
        private val mainStack: MainStack,
        private val subStack: Stack,
        override val cursor: CursorInfo
) : TouchInputListener {

    override fun listeningActions(): Set<TouchInputStatus> = setOf(TOUCH)

    override fun onAction() {
        if (mainStack.isNotInShape(cursor.pos)) return

        when {
            mainStack.isNotEmpty() -> {
                logger.debug("main -> move one card to sub")

                subStack.add(mainStack.removeLast().apply { open(true); touchable(true) })

                commitTasks()
            }
            mainStack.isEmpty() -> {
                logger.debug("main -> return all cards to main")

                subStack.forEach { it.open(false) }
                mainStack.addAllAsReverser(subStack)
                subStack.clear()

                commitTasks()
            }
        }
    }

    private fun commitTasks() {
        val taskId = TaskManager.genId()
        GameActionHistory.addAction(MainStackTouchCancel(taskId))
        TaskManager.commit(CardPosition(taskId))
    }

    companion object {
        private val logger by lazy { loggerApp<MainStackListenerNew>() }
    }
}