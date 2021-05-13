package cardparser.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

interface MainStackAPI {
    var mainStackComp: MainStackComp

    fun order(): Int = mainStackComp.order
}

class MainStackComp : Component, Pool.Poolable, Comparable<MainStackComp> {
    var order = 0

    override fun compareTo(other: MainStackComp): Int = this.order.compareTo(other.order)

    override fun reset() = run { order = 0 }

    override fun toString(): String = "MainStackComponent"

    companion object {
        val mapper = mapperFor<MainStackComp>()
    }
}
