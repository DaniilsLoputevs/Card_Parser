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
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import ktx.ashley.exclude

class CalculateIsTouchableSystem(
        val gameEventManager: GameEventManager
) : IteratingSystem(
        allOf(TransformComp::class, StackComp::class).exclude(MainStackComp::class).get()
), GameEventListener {
    private val stack: Stack = Stack()

    lateinit var touchLogic: CalculateLogic

    private var dropEvent: GameEvent.DropEvent? = null


    override fun update(deltaTime: Float) {
        dropEvent?.let {
            if (findStacks(it.position)) {
                touchLogic.setTouchable(stack)
                touchLogic.setTouchable(it.previousStack)
                dropEvent = null
            }
        }

    }

    override fun processEntity(entity: Entity?, deltaTime: Float) = run { }


    private fun findStacks(pos: Vector2): Boolean {
        entities.forEach {
            stack.entity = it
            if (stack.containsPosInTotalHitBox(pos)) return true
        }
        return false
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
