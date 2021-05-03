package cardparser.ashley.systems

import cardparser.ashley.components.FoundationStackComponent
import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude

@Deprecated("")
class TouchOpenCardSystem(val gameEventManager: GameEventManager): IteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class)
        .exclude(FoundationStackComponent::class).get()
), GameEventListener {

    var touchEvent: GameEvent.TouchEvent? = null

    override fun addedToEngine(engine: Engine?) {
        gameEventManager.addListener(GameEvent.TouchEvent::class, this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameEventManager.removeListener(GameEvent.TouchEvent::class, this)
        super.removedFromEngine(engine)
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        touchEvent = null
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        touchEvent?.let {
            val stack = GameStackAdapter(entity)
            if (stack.containsPosInTotalHitBox(it.position))
                stack.getCards().lastOrNull()?.let { card ->
                    if (!card.gameCardComp.isCardOpen) {
                        card.gameCardComp.isCardOpen = true
                    }
                }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.TouchEvent) {
            touchEvent = event
        }
    }
}
