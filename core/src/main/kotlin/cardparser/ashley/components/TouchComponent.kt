package cardparser.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Entity with this component: can bee touched by cursor.
 */
class TouchComponent : Component, Pool.Poolable {
    var isTouchable = true


    override fun reset() {
        isTouchable = true
    }

    override fun toString(): String = "TouchComponent"

    companion object {
        val mapper = mapperFor<TouchComponent>()
    }

}
