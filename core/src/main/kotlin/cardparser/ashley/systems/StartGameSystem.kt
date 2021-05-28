package cardparser.ashley.systems

import cardparser.STACK_START_SPEED
import cardparser.ashley.logics.StartGameLogic
import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.event.GameEvent
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.EntitySystem

@Deprecated("off")
class StartGameSystem : EntitySystem() {

    private var accumulate = 0f
    lateinit var logic: StartGameLogic
    lateinit var cards: MutableList<Card>
    lateinit var startStack: Stack
    lateinit var stackList: List<Stack>

    override fun update(deltaTime: Float) {
//        logger.info("execute :: START")
        accumulate += deltaTime
        if (accumulate > STACK_START_SPEED) {
            if (!logic.start(cards, startStack, stackList)) {
                setProcessing(false)
                startStack.addAll(cards)
                GameEventManager.dispatchEvent(GameEvent.StartGame.apply { eventId = GameEventManager.genId() })
                logger.info("execute :: FINISH")
            }
            accumulate = 0f
        }
    }


    companion object {
        private val logger = loggerApp<StartGameSystem>()
    }
}
