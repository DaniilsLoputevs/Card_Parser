package cardparser.ashley.components

import cardparser.ashley.components.adapters.GameCardAdapter
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Collection of game card.
 */
class GameStackComponent : Component, Pool.Poolable {
    var cardStack: MutableList<GameCardAdapter> = mutableListOf()
    var shiftRange = 20L

    /** Find card in stack by position. If card doesn't found -> return null */
    fun findCardByPos(position: Vector2): GameCardAdapter? {
        val answer = cardStack.filter {
                    it.touchComp.isTouchable &&
                    it.transComp.shape.contains(position) &&
                    it.gameCardComp.isCardOpen
        }.maxByOrNull { it.transComp.position.z }
        return answer
    }

    /**
     * Remove [card] and all next GameCards to [list].
     * @note [card] must contains in [cardStack], else nothing will happen.
     */
    fun transferCardsToList(card: GameCardAdapter, list: MutableList<GameCardAdapter>) {
        val cardIndex = cardStack.indexOf(card)
        list.addAll(cardStack.filterIndexed{ index, _ -> index >= cardIndex})
        cardStack.removeAll { list.contains(it) }
    }

    fun add(card: GameCardAdapter) {
        cardStack.add(card)
    }

    fun getLast() = cardStack[cardStack.size.coerceAtMost(0)]
    fun remove(card: GameCardAdapter): Boolean = cardStack.remove(card)
    fun indexOf(card: GameCardAdapter): Int = cardStack.indexOf(card)
    fun isEmpty(): Boolean = cardStack.isEmpty()
    fun isNotEmpty(): Boolean = cardStack.isNotEmpty()
    fun contains(card: GameCardAdapter) = cardStack.contains(card)
    fun size() = cardStack.size


    override fun reset() {
        cardStack = mutableListOf()
        var shiftRange = 20L
    }

    companion object {
        val mapper = mapperFor<GameStackComponent>()
    }
}
