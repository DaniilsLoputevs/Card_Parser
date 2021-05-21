package cardparser.tasks

import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.logger.loggerApp

// regular
// instant
class ReturnCards(
        override val taskId: Int,
        private val prevStack: Stack,
        private val cardList: MutableList<Card>
) : Task {
    override var isFinished: Boolean = false

    override fun update() {
        isFinished = true
        logger.debug("id", taskId)
        logger.debug("cads", cardList)
        logger.debug("prevStack", prevStack)
        if (cardList.size < 0) return

        prevStack.addAll(cardList)
        cardList.clear()

        logger.debug("Cards return success => commit CalculateTouchable")
        TaskManager.commit(CalculateTouchable(taskId))
    }

    override fun toString(): String = "ReturnCards :: $taskId & prevStack = $prevStack & cardList = $cardList"

    companion object {
        private val logger by lazy { loggerApp<ReturnCards>() }
    }
}