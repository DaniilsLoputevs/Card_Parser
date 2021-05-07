package cardparser.ashley.systems

import cardparser.STACK_START_SPEED
import cardparser.ashley.StartGameLogic
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.EntitySystem

class StartGameSystem() : EntitySystem() {

    var accumulate = 0f
    var stepTime = 0.25f
    lateinit var logic: StartGameLogic
    lateinit var cards: MutableList<GameCardAdapter>
    lateinit var stack: GameStackAdapter
    lateinit var stackList: List<GameStackAdapter>
    lateinit var touchSystem: EntitySystem

    override fun update(deltaTime: Float) {
        accumulate += deltaTime
        if (accumulate > STACK_START_SPEED) {
            if (!logic.doLogic(cards, stack, stackList)) {
                setProcessing(false)
                touchSystem.setProcessing(true)
            }
            accumulate = 0f
        }
    }
}
