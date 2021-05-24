package cardparser.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2
import ktx.math.vec3

interface TransformAPI {
    var transComp: TransformComp

    fun pos(): Vector3 = transComp.position
    fun transfSize(): Vector2 = transComp.size

    /** Setters for position */
    fun setPos(newPos: Vector2, z: Float = transComp.position.z) = run { transComp.setPos(newPos.x, newPos.y, z) }
    fun setPos(x: Float = transComp.position.x, y: Float = transComp.position.y, z: Float = transComp.position.z) = run { transComp.setPos(x, y, z) }
    fun setPos(newPos: Vector3) = run { transComp.setPos(newPos.x, newPos.y, newPos.z) }

    /** Getters for position */
    fun getPos(posBuffer: Vector2) = run { posBuffer.apply { x = transComp.position.x; y = transComp.position.y; } }
    fun getPos(posBuffer: Vector3) = run { posBuffer.apply { x = transComp.position.x; y = transComp.position.y; z = transComp.position.z } }

    fun isInShape(position: Vector2) = this.transComp.shape.contains(position)
    fun isNotInShape(position: Vector2) = !this.transComp.shape.contains(position)

}

class TransformComp : Component, Pool.Poolable, Comparable<TransformComp> {
    val position = vec3()          // Vector3(0, 0, 0)
    val size = vec2(1f, 1f)  // x = width, y = height
    val shape = Rectangle()


    fun initThis(textureForSize: TextureAtlas.AtlasRegion,
                 posX: Float = 0f, posY: Float = 0f, posZ: Float = 0f) {
        initThis(
                textureForSize.originalWidth.toFloat(),
                textureForSize.originalHeight.toFloat(),
                posX, posY, posZ
        )
    }

    fun initThis(textureForSize: Texture,
                 posX: Float = 0f, posY: Float = 0f, posZ: Float = 0f) {
        initThis(
                textureForSize.width.toFloat(),
                textureForSize.height.toFloat(),
                posX, posY, posZ
        )
    }

    fun initThis(width: Float, height: Float,
                 posX: Float = 0f, posY: Float = 0f, posZ: Float = 0f) {
        this.size.set(width, height)
        this.position.set(posX, posY, posZ)
        this.shape.set(posX, posY, width, height)
    }

    fun setPos(x: Float = position.x, y: Float = position.y, z: Float = position.z) {
        position.x = x; position.y = y; position.z = z
        shape.setPosition(x, y)
    }

    fun setSize(width: Float, height: Float) {
        size.set(width, height)
        shape.setSize(width, height)
    }

    /**
     * Set new this.size by set ne height and save Aspect Ration, - find and set new Width for new height.
     * Example:
     * 1000 x 1500 (width x height)
     * newHeight = 750 =>>
     * 500 x 750 (width x height)
     * automatically find and set new values.
     */
    fun setSizeByHeightSAR(newHeight: Float) {
        val ratio = size.y / size.x //  (height / width)
        val newWidth = newHeight / ratio
        size.set(newWidth, newHeight)
        shape.setSize(newWidth, newHeight)
    }

    /**
     * Set new this.size by set new Width and save Aspect Ration, - find and set new Width for new height.
     * Example:
     * 1000 x 1500 (width x height)
     * newWidth = 500 =>>
     * 500 x 750 (width x height)
     * automatically find and set new values.
     */
    fun setSizeByWidthSAR(newWidth: Float) {
        val ratio = size.x / size.y //  (width / height)
        val newHeight = newWidth / ratio
        size.set(newWidth, newHeight)
        shape.setWidth(newWidth)
        shape.setHeight(newHeight)
    }


    /* Structure stuff */


    override fun reset() {
        position.set(Vector3.Zero)
        size.set(1f, 1f)
        shape.set(0f, 0f, 0f, 0f)
    }

    override fun compareTo(other: TransformComp): Int {
        val zDiff = position.z.compareTo(other.position.z)
        return if (zDiff == 0) other.position.y.compareTo(position.y) else zDiff
    }

    override fun toString(): String = "TransformComponent"

    companion object {
        val mapper = mapperFor<TransformComp>()
    }
}
