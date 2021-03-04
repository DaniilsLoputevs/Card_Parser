package towerdefense.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import towerdefense.ashley.components.RemoveComponent
import towerdefense.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.get
import towerdefense.ashley.components.MoveComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.findComponent

class RemoveSystem(
    private val gameEventManager: GameEventManager
) : IteratingSystem(allOf(RemoveComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removeComp = entity.findComponent(RemoveComponent.mapper)

        removeComp.delay -= deltaTime
        if (removeComp.delay <= 0f) {
//            entity[PlayerComponent.mapper]?.let { player ->
//                gameEventManager.dispatchEvent(GameEvent.PlayerDeath.apply {
//                    distance = player.distance
//                })
//            }

            engine.removeEntity(entity)
        }
    }
}
