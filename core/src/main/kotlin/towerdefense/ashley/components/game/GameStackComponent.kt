package towerdefense.ashley.components.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class GameStackComponent : Component, Pool.Poolable {
    var cardStack: MutableList<Entity> = mutableListOf()

    fun isEmpty() = cardStack.isEmpty()
    fun addGameCard(card: Entity) = cardStack.add(card)
    fun getLastCard() = cardStack[cardStack.size.coerceAtMost(0)]

    //    fun getRemoveCard() = cardStack.removeAt(cardStack.size.coerceAtMost(0))
    fun removeGameCard(card: Entity) = cardStack.remove(card)

    fun size() = cardStack.size

    /**
     * TODO - maybe nee to implement "manual contains", cause Entity DON'T override equals
     */
    fun contains(card: Entity) = cardStack.contains(card)

    override fun reset() {
        cardStack = mutableListOf()
    }

    companion object {
        val mapper = mapperFor<GameStackComponent>()
    }

    override fun toString(): String {
        return cardStack.toString()
    }
}