package cardparser.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class WinComp : Component, Pool.Poolable  {
    var total: Int = 50

    override fun reset() {
        total = 50
    }

    override fun toString(): String {
        return super.toString()
    }

    companion object {
        val mapper = mapperFor<WinComp>()
    }
}
