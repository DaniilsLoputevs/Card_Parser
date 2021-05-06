package cardparser.ashley.systems

import cardparser.UPDATE_RATE
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.ashley.allOf
import ktx.ashley.get

class CardPositionSystem : IteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class).get()
) {
    var step = 0F
    var z = 0F
    val nextPosition = Vector2.Zero

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val stack = GameStackAdapter(entity)
        step = 0F
        z = 0F
        stack.getCards().forEach {
            nextPosition.set(stack.transComp.position.x, stack.transComp.position.y - step)
            it.transComp.run {
                val alpha = UPDATE_RATE
                if (near(nextPosition, position)) {
                    setPosition(
                        MathUtils.lerp(position.x, nextPosition.x, alpha*2),
                        MathUtils.lerp(position.y, nextPosition.y, alpha*2),
                        z +100
                    )
                } else {
                    position.set(
                        MathUtils.lerp(position.x, nextPosition.x, alpha),
                        MathUtils.lerp(position.y, nextPosition.y, alpha),
                        z + 300
                    )
                }
            }
            step += stack.gameStackComp.shiftRange
            z++
        }
    }

    private fun near(vect2: Vector2, vect3: Vector3): Boolean {
        return vect2.x - vect3.x <= 100 && vect2.x - vect3.x >= -100 &&
                vect2.y - vect3.y <= 100 && vect2.y - vect3.y >= -100
    }


}
