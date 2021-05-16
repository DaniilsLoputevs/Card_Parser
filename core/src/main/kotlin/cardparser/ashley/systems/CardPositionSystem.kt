package cardparser.ashley.systems

import cardparser.MAX_SPEED_RATE
import cardparser.MIN_SPEED_RATE
import cardparser.ashley.components.StackComp
import cardparser.ashley.components.TransformComp
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.lerp
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.ashley.allOf

class CardPositionSystem : IteratingSystem(
        allOf(TransformComp::class, StackComp::class).get()
//                ALL_STACK_FAMILY
) {


    private val stack = Stack()

    private var step = 0F
    private var z = 0F
    private var nextZ = 0F
    private val nextPos: Vector2 = Vector2.Zero


    override fun update(deltaTime: Float) {
        z = 0F
        entities.forEach { processEntity(it, deltaTime) }
    }

    /** process each stack */
    override fun processEntity(entity: Entity, deltaTime: Float) {
        stack.entity = entity
        step = 0F

        stack.cards().forEachIndexed { index, card ->
            nextPos.set(stack.pos().x, stack.pos().y - step)
            if (isNear(nextPos, card.pos())) {
                card.setPos(
                        lerp(card.pos().x, nextPos.x, MAX_SPEED_RATE),
                        lerp(card.pos().y, nextPos.y, MAX_SPEED_RATE),
                        z + 1
                )
            } else {
                nextZ = if (index == 0) card.pos().z + z
                else {
                    val prevCardDepth = stack[index - 1].pos().z
                    if (prevCardDepth > card.pos().z) prevCardDepth + z
                    else card.pos().z + z
                }

                card.setPos(
                        lerp(card.pos().x, nextPos.x, MIN_SPEED_RATE),
                        lerp(card.pos().y, nextPos.y, MIN_SPEED_RATE),
                        nextZ
                )
            }
            step += if (card.open()) stack.stackComp.shiftRange
             else (stack.stackComp.shiftRange / 1.5).toLong()

            z++
        }
    }

    private fun isNear(vect2: Vector2, vect3: Vector3): Boolean {
        return vect2.x - vect3.x <= 50 && vect2.x - vect3.x >= -50 &&
                vect2.y - vect3.y <= 50 && vect2.y - vect3.y >= -50
    }

    companion object {
        private val logger = loggerApp<CardPositionSystem>()
    }
}
