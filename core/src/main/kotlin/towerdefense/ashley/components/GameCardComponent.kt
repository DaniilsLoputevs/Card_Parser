package towerdefense.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * This component say: This Entity is Game Card.
 */
class GameCardComponent : Component, Pool.Poolable{

    override fun reset() {

    }


    companion object {
        val mapper = mapperFor<GameCardComponent>()
    }

}
