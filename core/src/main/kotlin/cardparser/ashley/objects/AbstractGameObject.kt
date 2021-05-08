package cardparser.ashley.objects

import cardparser.ashley.components.TransformComponent
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.ashley.get

private val DEFAULT_ENTITY = Entity()

private val logger = loggerApp<AbstractGameObject>()

abstract class AbstractGameObject {
    var entity: Entity = DEFAULT_ENTITY
        set(value) {
            if (value == DEFAULT_ENTITY) {
                logger.warm("Try to set EMPTY entity to AbstractAdapter.entity"); return
            }
            field = value
            refreshInnerState()
            refreshState()
        }
    lateinit var transf: TransformComponent
    lateinit var pos: Vector3
    lateinit var size: Vector2
    lateinit var shape: Rectangle

    /** Setters for position */
    fun setPos(newPos: Vector2, z: Float = pos.z) = run { transf.setPos(newPos.x, newPos.y, z) }
    fun setPos(x: Float = pos.x, y: Float = pos.y, z: Float = pos.z) = run { transf.setPos(x, y, z) }

    /** Getters for position */
    fun getPos(posBuffer: Vector2) = run { posBuffer.apply { x = pos.x; y = pos.y; } }
    fun getPos(posBuffer: Vector3) = run { posBuffer.apply { x = pos.x; y = pos.y; z = pos.z } }

    fun isInShape(position: Vector2) = this.transf.shape.contains(position)


    private fun refreshInnerState() {
        this.transf = entity[TransformComponent.mapper]!!
        this.pos = transf.position
        this.size = transf.size
        this.shape = transf.shape
    }

    protected abstract fun refreshState()
}
