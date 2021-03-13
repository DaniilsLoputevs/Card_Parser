package towerdefense.ashley.components.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import towerdefense.gameStrucures.adapters.GameCardAdapter

class GameStackComponent : Component, Pool.Poolable {
    var cardStack: MutableList<GameCardAdapter> = mutableListOf()
    var onClick: () -> Unit = {}
    var onAddCard: () -> Unit = {}

    fun isEmpty() = cardStack.isEmpty()
    fun addGameCard(card: GameCardAdapter) {
        onAddCard.invoke()
        cardStack.add(card)
    }

    fun getLastCard() = cardStack[cardStack.size.coerceAtMost(0)]

    //    fun getRemoveCard() = cardStack.removeAt(cardStack.size.coerceAtMost(0))
    fun removeGameCard(card: GameCardAdapter) = cardStack.remove(card)

    fun size() = cardStack.size

    /**
     * TODO - maybe nee to implement "manual contains", cause Entity DON'T override equals
     */
    fun contains(card: GameCardAdapter) = cardStack.contains(card)


    /* Other functional */


    override fun reset() {
        cardStack = mutableListOf()
        onClick = {}
        onAddCard = {}
    }

    companion object {
        val mapper = mapperFor<GameStackComponent>()
    }

    override fun toString(): String = cardStack.toString()
}