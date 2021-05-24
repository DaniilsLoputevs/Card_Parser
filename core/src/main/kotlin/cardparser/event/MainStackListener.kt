package cardparser.event

import cardparser.entities.MainStack
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import cardparser.tasks.CardPosition
import cardparser.tasks.GameActionHistory
import cardparser.tasks.TaskManager
import cardparser.tasks.cancel.MainStackTouchCancel
import kotlin.reflect.KClass

class MainStackListener(private val mainStack: MainStack, private val subStack: Stack) : GameEventListener {

    override fun onEvent(event: GameEvent) {
        if (mainStack.isNotInShape((event as GameEvent.TouchEvent).position)) return

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
        if (mainStack.isEmpty() && subStack.isEmpty()) stopListeningMainStack()
    }

    private fun commitTasks() {
        val taskId = TaskManager.genId()
        GameActionHistory.addAction(MainStackTouchCancel(taskId))
        TaskManager.commit(CardPosition(taskId))
    }

    private fun stopListeningMainStack() = GameEventManager.removeListener(this)


    companion object {
        val eventTypes = listOf<KClass<out GameEvent>>(GameEvent.TouchEvent::class)

        private val logger by lazy { loggerApp<MainStackListener>() }
    }
}
