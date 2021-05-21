package cardparser.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

interface MainStackAPI {
    var mainStackComp: MainStackComp

    @Deprecated("For remove, after merge to main")
    fun order(): Int = mainStackComp.order

    fun isMain(): Boolean = mainStackComp.isItMainStackOrSub
}

class MainStackComp : Component, Pool.Poolable, Comparable<MainStackComp> {
    @Deprecated("For remove, after merge to main")
    var order = 0
    var isItMainStackOrSub = true

    @Deprecated("For remove, after merge to main")
    override fun compareTo(other: MainStackComp): Int = this.order.compareTo(other.order)

    @Deprecated("For remove, after merge to main")
    override fun reset() = run { order = 0 }

    override fun toString(): String = "MainStackComponent"

    companion object {
        val mapper = mapperFor<MainStackComp>()
    }
}
