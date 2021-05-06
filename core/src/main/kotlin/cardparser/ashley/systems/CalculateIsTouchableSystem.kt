package cardparser.ashley.systems

import cardparser.ashley.CalculateLogic
import cardparser.ashley.components.GameCardComponent
import cardparser.ashley.components.GameCardComponent.*
import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.MainStackComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude

class CalculateIsTouchableSystem(
    val gameEventManager: GameEventManager
) : IteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class).exclude(MainStackComponent::class).get()
), GameEventListener {
    private val logger = loggerApp<CalculateIsTouchableSystem>()
    lateinit var logic: CalculateLogic
    var dropEvent: GameEvent.DropEvent? = null
    var processed = false
    private val stack: GameStackAdapter = GameStackAdapter()


    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if (processed) {
            processed = false
            dropEvent = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        dropEvent?.let {
            processed = true
            stack.entity = entity
            logic.doLogic(stack)
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DropEvent) {
            dropEvent = event
        }
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        gameEventManager.addListener(GameEvent.DropEvent::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        gameEventManager.removeListener(GameEvent.DropEvent::class, this)
    }
}
