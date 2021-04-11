package towerdefense.ashley.components.KlondikeGame

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class KlondikeMainStackComponent : Component, Pool.Poolable, Comparable<KlondikeMainStackComponent> {

    var touched = false;
    var order = 0;

    override fun reset() {
        touched = false;
        order = 0;
    }

    companion object {
        val mapper = mapperFor<KlondikeMainStackComponent>()
    }

    override fun compareTo(other: KlondikeMainStackComponent): Int = this.order.compareTo(other.order)
}
