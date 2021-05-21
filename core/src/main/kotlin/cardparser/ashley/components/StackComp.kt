package cardparser.ashley.components

import cardparser.ashley.StackCanAddLogic
import cardparser.entities.Card
import cardparser.entities.Stack
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

interface StackAPI {
    var stackComp: StackComp

    operator fun get(index: Int) = stackComp.cardStack[index]
    operator fun get(position: Vector2) = findCardByPos(position)
    fun cards(): MutableList<Card> = stackComp.cardStack

    /** If card doesn't found -> return null */
    fun findCardByPos(position: Vector2): Card? {
        return stackComp.cardStack
                .filter { it.open() && it.touchable() && it.isInShape(position) }
                .maxByOrNull { it.pos().z }
    }

    /**
     * Remove [card] and all next GameCards to [list].
     * @note [card] must contains in [list], else nothing will happen.
     */
    fun transferCardsToList(card: Card, list: MutableList<Card>) {
        val cardIndex = stackComp.cardStack.indexOf(card)
        list.addAll(stackComp.cardStack.filterIndexed { index, _ -> index >= cardIndex })
        stackComp.cardStack.removeAll { list.contains(it) }
    }

    fun canAddCards(cards: List<Card>) = stackComp.stackCanAddLogic.check(this, cards)

    /* Collection API */
    fun forEach(action: (each: Card) -> Unit): Unit = stackComp.cardStack.forEach(action)
    fun forEachIndexed(action: (i: Int, each: Card) -> Unit): Unit = stackComp.cardStack.forEachIndexed(action)
    fun add(card: Card) = stackComp.cardStack.add(card)
    fun addAll(cards: Iterable<Card>) = stackComp.cardStack.addAll(cards)
    fun addAllAsReverser(cards: MutableList<Card>) = addAllAsReverserIImpl(cards)
    fun addAllAsReverser(stack: Stack) = addAllAsReverserIImpl(stack.cards())
    fun getLast() = stackComp.cardStack[stackComp.cardStack.size.coerceAtMost(0)]
    fun remove(card: Card): Boolean = stackComp.cardStack.remove(card)
    fun removeLast(): Card = stackComp.cardStack.removeLast()
    fun removeAt(index: Int): Card = stackComp.cardStack.removeAt(index)
    fun removeAll(cards: Iterable<Card>) = stackComp.cardStack.removeAll(cards)
    fun last() = stackComp.cardStack.last()
    fun asReversed() = stackComp.cardStack.asReversed()
    fun indexOf(card: Card): Int = stackComp.cardStack.indexOf(card)
    fun isEmpty(): Boolean = stackComp.cardStack.isEmpty()
    fun isNotEmpty(): Boolean = stackComp.cardStack.isNotEmpty()
    fun clear() = stackComp.cardStack.clear()
    fun contains(card: Card) = stackComp.cardStack.contains(card)
    fun size() = stackComp.cardStack.size

    private fun addAllAsReverserIImpl(cards: MutableList<Card>) {
        for (index in cards.lastIndex downTo 0) add(cards[index])
    }
}

class StackComp : Component, Pool.Poolable {
    var cardStack: MutableList<Card> = mutableListOf()
    var shiftStep = 20L
    lateinit var stackCanAddLogic: StackCanAddLogic


    override fun reset() {
        cardStack = mutableListOf()
        shiftStep = 20L
    }

    override fun toString(): String = "stack comp ={ shiftRange = $shiftStep & size = ${cardStack.size} }"

    companion object {
        val mapper = mapperFor<StackComp>()
    }
}
