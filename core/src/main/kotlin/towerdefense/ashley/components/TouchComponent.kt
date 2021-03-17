package towerdefense.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Entity with this component: can bee touched by cursor.
 */
class TouchComponent : Component, Pool.Poolable{
    var isTouchable = true

//    val prevPosition = vec3()           // Vector3(0, 0, 0)
//    val position = vec3()               // Vector3(0, 0, 0)
//    val interpolatedPosition = vec3()   // Vector3(0, 0, 0)
//    val size = vec2(1f, 1f)
//    var rotationDeg = 0f

    override fun reset() {
        isTouchable = true
//        prevPosition.set(Vector3.Zero)
//        position.set(Vector3.Zero)
//        interpolatedPosition.set(Vector3.Zero)
//        size.set(1f, 1f)
//        rotationDeg = 0f
    }




    companion object {
        val mapper = mapperFor<TouchComponent>()
    }




}
