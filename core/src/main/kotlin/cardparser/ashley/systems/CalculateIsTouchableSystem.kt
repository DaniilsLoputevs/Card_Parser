package cardparser.ashley.systems

import cardparser.ashley.StackTouchLogic
import cardparser.ashley.components.MainStackComp
import cardparser.ashley.components.StackComp
import cardparser.ashley.components.TransformComp
import cardparser.entities.Stack
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude

@Deprecated("off")
class CalculateIsTouchableSystem
    : IteratingSystem(allOf(TransformComp::class, StackComp::class).exclude(MainStackComp::class).get()
), GameEventListener {
//class CalculateIsTouchableSystem(val gameEventManager: GameEventManager)
//    : IteratingSystem(STACK_FAMILY), GameEventListener {


    private val stack: Stack = Stack()

    lateinit var touchTouchLogic: StackTouchLogic

    private var dropEvent: GameEvent.DropEvent? = null
    private var log = true

    override fun update(deltaTime: Float) {
        dropEvent?.let {
            entities.forEach {
                stack.entity = it
                touchTouchLogic.setTouchable(stack)
            }
            dropEvent = null
        }

    }

    override fun processEntity(entity: Entity?, deltaTime: Float) = run { }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DropEvent) {
            dropEvent = event
        }
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        GameEventManager.addListener(GameEvent.DropEvent::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        GameEventManager.removeListener(GameEvent.DropEvent::class, this)
    }

    companion object {
        private val logger = loggerApp<CalculateIsTouchableSystem>()
    }
}
