package cardparser.ashley.systems

import cardparser.ashley.CalculateLogic
import cardparser.ashley.components.MainStackComp
import cardparser.ashley.components.StackComp
import cardparser.ashley.components.TransformComp
import cardparser.ashley.entities.Stack
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
        allOf(TransformComp::class, StackComp::class).exclude(MainStackComp::class).get()
), GameEventListener {

    lateinit var logic: CalculateLogic
    var dropEvent: GameEvent.DropEvent? = null
    var processed = false
    private val stack: Stack = Stack()


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

    companion object {
        private val logger = loggerApp<CalculateIsTouchableSystem>()
    }
}
