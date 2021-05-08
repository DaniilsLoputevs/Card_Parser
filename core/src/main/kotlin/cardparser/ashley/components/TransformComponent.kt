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

class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent> {
    val position = vec3()          // Vector3(0, 0, 0)
    val prevPosition = vec3()
    val size = vec2(1f, 1f)  // x = width, y = height
    val shape = Rectangle()


    fun initTransformComp(textureForSize: TextureAtlas.AtlasRegion,
                          posX: Float = 0f, posY: Float = 0f, posZ: Float = 0f) {
        initTransformComp(
                textureForSize.originalWidth.toFloat(),
                textureForSize.originalHeight.toFloat(),
                posX, posY, posZ
        )
    }

    fun initTransformComp(textureForSize: Texture,
                          posX: Float = 0f, posY: Float = 0f, posZ: Float = 0f) {
        initTransformComp(
                textureForSize.width.toFloat(),
                textureForSize.height.toFloat(),
                posX, posY, posZ
        )
    }

    fun initTransformComp(
            width: Float, height: Float,
            posX: Float = 0f, posY: Float = 0f, posZ: Float = 0f
    ) {
        this.size.set(width, height)
        this.position.set(posX, posY, posZ)
        this.shape.set(posX, posY, width, height)
    }

    fun setPosition(newPosition: Vector2) {
        position.set(newPosition, position.z)
        shape.setPosition(newPosition.x, newPosition.y)
    }

    fun setPosition(newPosition: Vector3) {
        position.set(newPosition)
        shape.setPosition(newPosition.x, newPosition.y)
    }

    fun setPosition(x: Float = position.x, y: Float = position.y, z: Float = position.z) {
        position.x = x
        position.y = y
        position.z = z
        shape.setPosition(x,y)
    }

    fun setX(x: Float) {
        position.x = x
        shape.x = x
    }

    fun setY(y: Float) {
        position.y = y
        shape.y = y
    }

    fun setDepth(z: Float) {
        position.z = z
    }

    fun getDepth() = position.z

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

    override fun compareTo(other: TransformComponent): Int {
        val zDiff = position.z.compareTo(other.position.z)
        return if (zDiff == 0) other.position.y.compareTo(position.y) else zDiff
    }

    override fun toString(): String = "TransformComponent"

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}
