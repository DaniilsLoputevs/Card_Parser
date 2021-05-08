package cardparser.ashley.systems

import cardparser.STACK_START_SPEED
import cardparser.ashley.StartGameLogic
import cardparser.ashley.objects.Card
import cardparser.ashley.objects.Stack
import cardparser.event.GameEvent
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.EntitySystem

class StartGameSystem(val gameEventManager: GameEventManager) : EntitySystem() {

    var accumulate = 0f
    lateinit var logic: StartGameLogic
    lateinit var cards: MutableList<Card>
    lateinit var stack: Stack
    lateinit var stackList: List<Stack>

    override fun update(deltaTime: Float) {
        accumulate += deltaTime
        if (accumulate > STACK_START_SPEED) {
            if (!logic.doLogic(cards, stack, stackList)) {
                setProcessing(false)
                stack.cards.addAll(cards)
                gameEventManager.dispatchEvent(GameEvent.StartGame)
            }
            accumulate = 0f
        }
    }

    companion object {
        private val logger = loggerApp<StartGameSystem>()
    }
}
