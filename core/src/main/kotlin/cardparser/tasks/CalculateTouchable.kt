package cardparser.tasks

import cardparser.ashley.StackTouchLogic
import cardparser.entities.Stack
import cardparser.logger.loggerApp

// regular
// instant
class CalculateTouchable(override val taskId: Int) : Task {
    override var isFinished: Boolean = false

    override fun update() {
        logger.debug("CalculateTouchable update")
        botUpStacks.forEach(touchTouchLogic::setTouchable)
        isFinished = true
        TaskManager.commit(CardPosition(taskId))
    }

    override fun toString(): String = "CalculateTouchable :: $taskId"


    companion object {
        lateinit var botUpStacks: MutableList<Stack>
        lateinit var touchTouchLogic: StackTouchLogic

        private val logger by lazy { loggerApp<CalculateTouchable>() }
    }
}