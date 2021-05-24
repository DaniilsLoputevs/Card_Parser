package cardparser.tasks.cancel

import cardparser.entities.Card
import cardparser.entities.MainStack
import cardparser.entities.Stack
import cardparser.tasks.CalculateTouchable
import cardparser.tasks.Task
import cardparser.tasks.TaskManager

class BindingCancel(
        override val taskId: Int,
        var currStack: Stack,
        var prevStack: Stack,
        var cardList: MutableList<Card>
) : Task {
    override var isFinished: Boolean = false

    override fun update() {
        if (prevStack is MainStack) prevStack.last().apply { this.touchable(false); open(false) }

        currStack.removeAll(cardList)
        prevStack.addAll(cardList)
        cardList.clear()
        isFinished = true
        TaskManager.commit(CalculateTouchable(taskId))
    }

    override fun toString(): String = "BindingCancel :: $taskId currStack = $currStack & prevStack = $prevStack & cardList = $cardList"

}