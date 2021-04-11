package towerdefense.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class FoundationStackComponent: Component, Pool.Poolable {

    override fun reset() {
    }

    companion object {
        val mapper = mapperFor<FoundationStackComponent>()
    }

}
