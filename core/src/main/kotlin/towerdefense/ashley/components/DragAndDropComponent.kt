package towerdefense.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2
import ktx.math.vec3

/**
 * Entity with this property can be moved to other place by drag&drop'ed.
 */
class DragAndDropComponent : Component, Pool.Poolable{
    var isClickable = true

//    val prevPosition = vec3()           // Vector3(0, 0, 0)
//    val position = vec3()               // Vector3(0, 0, 0)
//    val interpolatedPosition = vec3()   // Vector3(0, 0, 0)
//    val size = vec2(1f, 1f)
//    var rotationDeg = 0f

    override fun reset() {
//        prevPosition.set(Vector3.Zero)
//        position.set(Vector3.Zero)
//        interpolatedPosition.set(Vector3.Zero)
//        size.set(1f, 1f)
//        rotationDeg = 0f
    }




    companion object {
        val mapper = mapperFor<DragAndDropComponent>()
    }




}
