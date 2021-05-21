package cardparser.ashley.systems

import cardparser.ashley.components.MainStackComp
import cardparser.ashley.components.StackComp
import cardparser.ashley.components.TransformComp
import cardparser.entities.Stack
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import cardparser.tasks.ReturnCards
import cardparser.tasks.TaskManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude

@Deprecated("off")
class StackBindingSystem : IteratingSystem(
        allOf(TransformComp::class, StackComp::class)
                .exclude(MainStackComp::class).get()
), GameEventListener {
    private val stack = Stack()
    private var dropEvent: GameEvent.DropEvent? = null
    private var isBindingSuccess = false

    private val taskIdStub = 777


    override fun update(deltaTime: Float) {
        dropEvent?.let { event ->
            logger.debug("Try to bind cards")
            logger.debug("cardList", event.cardList)
            entities.forEach { processEntity(it, deltaTime) }

            logger.dev("dev event", event)
            if (!isBindingSuccess) TaskManager.commit(ReturnCards(taskIdStub, event.prevStack, event.cardList))
            dropEvent = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        dropEvent?.let {
            stack.entity = entity
            if (stack.containsPos(it.position) && it.cardList.size > 0 && stack.canAddCards(it.cardList)) {
                stack.addAll(it.cardList)
                it.cardList.clear()
                dropEvent = null
                logger.debug("Cards bind success")
                isBindingSuccess = true
            }
        }
    }

    override fun addedToEngine(engine: Engine?) {
        GameEventManager.addListener(GameEvent.DropEvent::class, this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        GameEventManager.removeListener(GameEvent.DropEvent::class, this)
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
