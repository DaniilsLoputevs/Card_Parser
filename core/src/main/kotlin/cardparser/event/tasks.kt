package cardparser.event

import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.logger.loggerApp

interface Task {
    //    fun run()
    fun update()
    fun isFinished(): Boolean
}

class ReturnCards(
        private val prevStack: Stack = Stack(),
        private val cardList: MutableList<Card> = mutableListOf()
) : Task {
    private val logger = loggerApp<ReturnCards>()

    private var isFinished = false


    override fun toString(): String {
        return "ReturnCards -- prevStack = ${GameEvent.DropEvent.prevStack} & cardList = $cardList"
    }


    override fun update() {
        logger.dev("Try to return cards", cardList)
        if (cardList.size > 0) {
            prevStack.addAll(cardList)
            cardList.clear()
            logger.dev("Cards return success")
            isFinished = true
        }
    }

    override fun isFinished(): Boolean = isFinished

}

object TaskManager {
    private val tasks: MutableList<Task> = mutableListOf()

    fun dispatchTask(task: Task) {
        tasks.add(task)
    }

    fun update() {
        tasks.removeAll { it.isFinished() }
        tasks.forEach(Task::update)
    }
}