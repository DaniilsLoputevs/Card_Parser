package cardparser.ashley.systems

import cardparser.STACK_START_SPEED
import cardparser.ashley.StartGameLogic
import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.event.GameEvent
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.EntitySystem

class StartGameSystem : EntitySystem() {

    var accumulate = 0f
    lateinit var logic: StartGameLogic
    lateinit var cards: MutableList<Card>
    lateinit var stack: Stack
    lateinit var stackList: List<Stack>

    override fun update(deltaTime: Float) {
//        logger.info("execute :: START")
        accumulate += deltaTime
        if (accumulate > STACK_START_SPEED) {
            if (!logic.doLogic(cards, stack, stackList)) {
                setProcessing(false)
                stack.cards().addAll(cards)
                GameEventManager.dispatchEvent(GameEvent.StartGame)
                logger.info("execute :: FINISH")
            }
            accumulate = 0f
        }
    }


    companion object {
        private val logger = loggerApp<StartGameSystem>()
    }
}
