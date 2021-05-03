package cardparser.ashley.systems

import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.GameStackComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get

class CardPositionSystem : IteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transStack = entity[TransformComponent.mapper]
        require(transStack != null) { "CardAdapter don't have TransformComponent" }
        val gameStack = entity[GameStackComponent.mapper]
        require(gameStack != null) { "CardAdapter don't have GameStackComponent" }

        var step = 0F
        var z = 0F
        if (gameStack.cardStack.size > 0) {
        }
        gameStack.cardStack.forEach {
            it.transComp.setX(transStack.position.x)
            it.transComp.setY(transStack.position.y - step)
            it.transComp.setDepth(z)
            it.transComp.position.z = z
            step += gameStack.shiftRange
            z++
        }

    }
}
