package cardparser.ashley.systems

import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.MainStackComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.objects.Stack
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
        allOf(TransformComponent::class, GameStackComponent::class)
                .exclude(MainStackComponent::class).get()
), GameEventListener {

    var dropEvent: GameEvent.DropEvent? = null

    override fun addedToEngine(engine: Engine?) {
        gameEventManager.addListener(GameEvent.DropEvent::class, this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameEventManager.removeListener(GameEvent.DropEvent::class, this)
        super.removedFromEngine(engine)
    }

    override fun update(deltaTime: Float) {
        dropEvent?.let {
            super.update(deltaTime)
            dropEvent = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        dropEvent?.let {
            val stack = Stack(entity)
            if (stack.containsPos(it.position) && it.cardList.size > 0
                    && stack.gameStackComp.stackLogic.doLogic(stack, it.cardList)
            ) {
                stack.cards.addAll(it.cardList)
                it.cardList.clear()
                dropEvent = null
            }
        }
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
