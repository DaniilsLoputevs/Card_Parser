package cardparser.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

interface TouchAPI {
    var touchComp: TouchComp

    fun touchable(): Boolean = touchComp.isTouchable
    fun touchable(value: Boolean) = run { touchComp.isTouchable = value }
}

/**
 * Entity with this component: can bee touched by cursor.
 */
class TouchComp : Component, Pool.Poolable {
    var isTouchable = true


    override fun reset() {
        isTouchable = true
    }

    override fun toString(): String = "TouchComponent"

    companion object {
        val mapper = mapperFor<TouchComp>()
    }

}
