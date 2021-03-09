package towerdefense.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2
import ktx.math.vec3

class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent> {
    val prevPosition = vec3()           // Vector3(0, 0, 0)
    val position = vec3()               // Vector3(0, 0, 0)
    val interpolatedPosition = vec3()   // Vector3(0, 0, 0)
    val size = vec2(1f, 1f)
    var rotationDeg = 0f
    val shape = Rectangle()

    override fun reset() {
        prevPosition.set(Vector3.Zero)
        position.set(Vector3.Zero)
        interpolatedPosition.set(Vector3.Zero)
        size.set(1f, 1f)
        rotationDeg = 0f
    }

    private fun setInitialPosition(x: Float, y: Float, z: Float) {
        prevPosition.set(x, y, z)
        position.set(x, y, z)
        interpolatedPosition.set(x, y, z)
    }

    override fun compareTo(other: TransformComponent): Int {
        val zDiff = other.position.z.compareTo(position.z)
        return if (zDiff == 0) other.position.y.compareTo(position.y) else zDiff
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }


    /* CUSTOM STAFF */

    fun initTransformComp(textureForSize: TextureAtlas.AtlasRegion,
//                      width : Float, height : Float,
                          positionX: Float = 0f, positionY: Float = 0f,
                          rotationDeg: Float = 0f) {
//        println("DEV initTransformComp")
//        println("textureForSize.originalWidth = ${textureForSize.originalWidth.toFloat()}")
//        println("textureForSize.originalHeight = ${textureForSize.originalHeight.toFloat()}")
//        println("textureForSize.regionWidth = ${textureForSize.regionWidth.toFloat()}")
//        println("textureForSize.regionHeight = ${textureForSize.regionHeight.toFloat()}")
//        println("DEV initTransformComp")
        val width = textureForSize.originalWidth.toFloat()
        val height = textureForSize.originalHeight.toFloat()
        this.size.set(width, height)
        this.rotationDeg = rotationDeg
        this.setInitialPosition(positionX, positionY, 1f)
        this.shape.set(positionX, positionY, width, height)
    }

    /**
     * set position for all part of component
     */
    fun setTotalPosition(newPosition: Vector2) {
        prevPosition.set(position)
        position.set(newPosition, 0f)
        interpolatedPosition.set(newPosition, 0f)
        shape.setPosition(newPosition.x, newPosition.y)
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
        shape.setWidth(newWidth)
        shape.setHeight(newHeight)
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

}
