package cardparser.ashley.systems

import cardparser.MAX_SPEED_RATE
import cardparser.MIN_SPEED_RATE
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.adapters.GameStackAdapter
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.ashley.allOf

class CardPositionSystem : IteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class).get()
) {
    private var step = 0F
    private var z = 0F
    private var nextZ = 0F
    private val nextPosition: Vector2 = Vector2.Zero

    override fun update(deltaTime: Float) {
        z = 0F
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val stack = GameStackAdapter(entity)
        step = 0F

        stack.getCards().forEachIndexed { inx, it ->
            nextPosition.set(stack.transComp.position.x, stack.transComp.position.y - step)
            it.transComp.run {
                if (near(nextPosition, position)) {
                    setPosition(
                        MathUtils.lerp(position.x, nextPosition.x, MAX_SPEED_RATE),
                        MathUtils.lerp(position.y, nextPosition.y, MAX_SPEED_RATE),
                        z + 1
                    )
                } else {
                    nextZ = if (inx != 0 && stack.getCards()[inx - 1].transComp.position.z > position.z) {
                        stack.getCards()[inx - 1].transComp.position.z + z
                    } else {
                        position.z + z
                    }
                    position.set(
                        MathUtils.lerp(position.x, nextPosition.x, MIN_SPEED_RATE),
                        MathUtils.lerp(position.y, nextPosition.y, MIN_SPEED_RATE),
                        nextZ
                    )
                }
            }
            step += stack.gameStackComp.shiftRange
            z++
        }
    }

    private fun near(vect2: Vector2, vect3: Vector3): Boolean {
        return vect2.x - vect3.x <= 50 && vect2.x - vect3.x >= -50 &&
                vect2.y - vect3.y <= 50 && vect2.y - vect3.y >= -50
    }


}
