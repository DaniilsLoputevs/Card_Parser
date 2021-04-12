package towerdefense.ashley.components.klondike

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class MainStackComponent : Component, Pool.Poolable, Comparable<MainStackComponent> {

    var order = 0

    override fun reset() {
        order = 0
    }

    companion object {
        val mapper = mapperFor<MainStackComponent>()
    }

    override fun compareTo(other: MainStackComponent): Int = this.order.compareTo(other.order)
}
