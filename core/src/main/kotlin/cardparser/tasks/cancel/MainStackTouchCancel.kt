package cardparser.tasks.cancel

import cardparser.entities.MainStack
import cardparser.entities.Stack
import cardparser.tasks.CardPosition
import cardparser.tasks.Task
import cardparser.tasks.TaskManager

class MainStackTouchCancel(
        override val taskId: Int
) : Task {
    override var isFinished: Boolean = false


    override fun update() {

        if (subStack.isEmpty()) {
            // cancel move all

            mainStack.forEach { it.open(true) }
            subStack.addAllAsReverser(mainStack)
            mainStack.clear()

            TaskManager.commit(CardPosition(taskId))
        } else {
            // cancel move one card

            mainStack.add(subStack.removeLast().apply { open(false); touchable(false) })

            TaskManager.commit(CardPosition(taskId))
        }
        isFinished = true
    }

    override fun toString(): String = "MainStackTouchCancel :: $taskId"

    companion object {
        lateinit var mainStack: MainStack
        lateinit var subStack: Stack
    }
}