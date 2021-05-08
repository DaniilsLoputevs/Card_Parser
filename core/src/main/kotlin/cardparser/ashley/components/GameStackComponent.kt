package cardparser.ashley.components

import cardparser.ashley.StackLogic
import cardparser.ashley.objects.Card
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Collection of game card.
 */
class GameStackComponent : Component, Pool.Poolable {
    var cardStack: MutableList<Card> = mutableListOf()
    var shiftRange = 20L
    lateinit var stackLogic: StackLogic

    /** Find card in stack by position. If card doesn't found -> return null */
    fun findCardByPos(position: Vector2): Card? {
        return cardStack
                .filter { it.isOpen() && it.isTouchable() && it.isInShape(position) }
                .maxByOrNull { it.pos.z }
    }

    /**
     * Remove [card] and all next GameCards to [list].
     * @note [card] must contains in [cardStack], else nothing will happen.
     */
    fun transferCardsToList(card: Card, list: MutableList<Card>) {
        val cardIndex = cardStack.indexOf(card)
        list.addAll(cardStack.filterIndexed { index, _ -> index >= cardIndex })
        cardStack.removeAll { list.contains(it) }
    }

    fun add(card: Card) = cardStack.add(card)
    fun getLast() = cardStack[cardStack.size.coerceAtMost(0)]
    fun remove(card: Card): Boolean = cardStack.remove(card)
    fun indexOf(card: Card): Int = cardStack.indexOf(card)
    fun isEmpty(): Boolean = cardStack.isEmpty()
    fun isNotEmpty(): Boolean = cardStack.isNotEmpty()
    fun contains(card: Card) = cardStack.contains(card)
    fun size() = cardStack.size


    override fun reset() {
        cardStack = mutableListOf()
        var shiftRange = 20L
    }

    companion object {
        val mapper = mapperFor<GameStackComponent>()
    }
}
