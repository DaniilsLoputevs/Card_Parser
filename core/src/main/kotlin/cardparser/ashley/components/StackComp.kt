package cardparser.ashley.components

import cardparser.ashley.StackAddPredicate
import cardparser.ashley.entities.Card
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

    //    fun forEachIndexed(action: (i: Int, each : Card ) -> Unit): Unit =  stackComp.cardStack.forEachIndexed(action)
    fun canAddCards(cards: List<Card>) = stackComp.stackAddPredicate.check(this, cards)
    fun add(card: Card) = stackComp.cardStack.add(card)
    fun addAll(cards: Iterable<Card>) = stackComp.cardStack.addAll(cards)
    fun getLast() = stackComp.cardStack[stackComp.cardStack.size.coerceAtMost(0)]
    fun removeAt(index: Int): Card = stackComp.cardStack.removeAt(index)
    fun remove(card: Card): Boolean = stackComp.cardStack.remove(card)
    fun indexOf(card: Card): Int = stackComp.cardStack.indexOf(card)
    fun isEmpty(): Boolean = stackComp.cardStack.isEmpty()
    fun isNotEmpty(): Boolean = stackComp.cardStack.isNotEmpty()
    fun clear() = stackComp.cardStack.clear()
    fun contains(card: Card) = stackComp.cardStack.contains(card)
    fun size() = stackComp.cardStack.size
}

class StackComp : Component, Pool.Poolable {
    var cardStack: MutableList<Card> = mutableListOf()
    var shiftRange = 20L
    lateinit var stackAddPredicate: StackAddPredicate


    override fun reset() {
        cardStack = mutableListOf()
        var shiftRange = 20L
    }

    override fun toString(): String = "stack comp ={ shiftRange = $shiftRange & size = ${cardStack.size} }"

    companion object {
        val mapper = mapperFor<StackComp>()
    }
}
