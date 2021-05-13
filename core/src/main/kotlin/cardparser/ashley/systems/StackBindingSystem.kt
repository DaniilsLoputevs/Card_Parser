package cardparser.ashley.systems

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

class StackBindingSystem(val gameEventManager: GameEventManager) : IteratingSystem(
        allOf(TransformComp::class, StackComp::class)
                .exclude(MainStackComp::class).get()
), GameEventListener {

    private val stack = Stack()

    private var dropEvent: GameEvent.DropEvent? = null


    override fun update(deltaTime: Float) {
        dropEvent?.let {
            super.update(deltaTime)
            dropEvent = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        dropEvent?.let {
//            logger.dev("processEntity :: inner invoked")
            stack.entity = entity
//            logger.dev("stack", stack)
//            logger.dev("cardList", it.cardList)
            if (stack.containsPos(it.position) && it.cardList.size > 0 && stack.canAddCards(it.cardList)) {
                stack.addAll(it.cardList)
                it.cardList.clear()
                dropEvent = null
            }
        }
    }

    override fun addedToEngine(engine: Engine?) {
        gameEventManager.addListener(GameEvent.DropEvent::class, this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameEventManager.removeListener(GameEvent.DropEvent::class, this)
        super.removedFromEngine(engine)
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DropEvent) {
            dropEvent = event
        }
    }

    companion object {
        private val logger = loggerApp<StackBindingSystem>()
    }
}
