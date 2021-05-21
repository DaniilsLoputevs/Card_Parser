package cardparser.tasks

import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import cardparser.tasks.cancel.BindingCancel
import com.badlogic.gdx.math.Vector2

// regular
// instant
class StackBinding(
        override val taskId: Int,
        var prevStack: Stack,
        var cardList: MutableList<Card>,
        var position: Vector2
) : Task {
    override var isFinished: Boolean = false

    override fun update() {
        var isBindingSuccess = false
        var currStack = Stack()

        logger.debug("Try to bind cards :: taskId", taskId)
        if (prevStack.entity.components.size() > 0) logger.debug("prevStack", prevStack)
        else logger.error("prevStack is NONE")


        if (cardList.size > 0) botUpStacks.find { it.containsInArea(position) && it.canAddCards(cardList) }
                ?.let { currStack = it; it.addAll(cardList); isBindingSuccess = true }


        logger.debug("Cards bind success", isBindingSuccess)
        if (isBindingSuccess) GameActionHistory.addAction(BindingCancel(taskId, currStack, prevStack.cpy(), cardList.toMutableList()))

        if (isBindingSuccess) TaskManager.commit(CalculateTouchable(taskId))
        else TaskManager.commit(ReturnCards(taskId, prevStack, cardList))
        isFinished = true
    }

    override fun toString(): String = "StackBinding :: $taskId & pos = $position & prevStack = $prevStack & cardList = $cardList"

    companion object {
        lateinit var botUpStacks: MutableList<Stack>

        private val logger by lazy { loggerApp<StackBinding>() }
    }
}