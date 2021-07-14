package cardparser.ashley.systems

import cardparser.ashley.components.WinComp
import cardparser.entities.Stack
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf

class WinStackSystem : IteratingSystem(
    allOf(WinComp::class).get()
) {

    val stack = Stack()
    var total: Int = 100
    var current: Int = 0

    override fun update(deltaTime: Float) {
        current = 0
        entities.forEach {
            processEntity(it, deltaTime)
        }
        if (current >= total) {
            GameEventManager.dispatchEvent(GameEvent.WinGame)
            logger.debug("Win game event has occurred")
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        stack.entity = entity
        current += stack.stackComp.cardStack.size
    }

    companion object {
        private val logger = loggerApp<WinStackSystem>()
    }

}
