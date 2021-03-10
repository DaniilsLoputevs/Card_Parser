package towerdefense.ashley.components.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class GameStacksComponent : Component, Pool.Poolable {
    var cardStack: MutableList<Entity> = mutableListOf()

    fun isEmpty() = cardStack.isEmpty()
    fun addGameCard(card: Entity) = cardStack.add(card)
    fun getLastCard() = cardStack.removeAt(cardStack.size.coerceAtMost(0))
    fun removeGameCard(card: Entity) = cardStack.remove(card)

    /**
     * TODO - maybe nee to implement "manual contains", cause Entity DON'T override equals
     */
    fun contains(card: Entity) = cardStack.contains(card)

    override fun reset() {
        TODO("Not yet implemented")
    }

    companion object {
        val mapper = mapperFor<GameStacksComponent>()
    }
}