package towerdefense.ashley.components.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class GameStacksComponent: Component, Pool.Poolable {
     var cardStack : MutableList<Entity> = mutableListOf()

    fun getLastCard() : Entity {
        return cardStack.removeAt(cardStack.size)
    }

    override fun reset() {
        TODO("Not yet implemented")
    }

    companion object {
        val mapper = mapperFor<GameStacksComponent>()
    }
}