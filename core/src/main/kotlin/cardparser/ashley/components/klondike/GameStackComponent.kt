package cardparser.ashley.components.klondike

import cardparser.gameStrucures.adapters.GameCardAdapter
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Collection of game card.
 */
class GameStackComponent : Component, Pool.Poolable {
    var cardStack: MutableList<GameCardAdapter> = mutableListOf()

    @Deprecated("later, not now, for Maksim!!! DO THIS TASK by Haskell :D")
    var onClick: () -> Unit = {}
    @Deprecated("later, not now, for Maksim!!! DO THIS TASK by Haskell :D")
    var onAddCard: () -> Unit = {}
    var addCardPredicate: (GameCardAdapter, GameCardAdapter) -> Boolean = { _: GameCardAdapter, _: GameCardAdapter -> true }


    /** Find card in stack by position. If card doesn't found -> return null */
    fun findByPos(position: Vector2): GameCardAdapter? {
        return cardStack.filter { it.touchComp.isTouchable && it.transComp.shape.contains(position) }
                .maxByOrNull { it.transComp.position.z }
    }

    /** Transfer all elements start [fromIndex] to [touchList] collection. */
    fun transferElementsFromIndexToList(fromIndex: Int, touchList: MutableList<GameCardAdapter>) {
        for (index in fromIndex until cardStack.size) {
            touchList.add(cardStack[index])
        }
    }

    /**
     * Check that [card] pass all onAdd predicates. Call before use method: [add].
     *
     * If Empty:
     * 1) check: new card must be higher or equivalent to King.
     *
     * If NOT Empty:
     * 1) check: is it Self-Adding. (try to add card to current stack.
     * we can't add card to stack, what already contains this card.)
     * 2) check: test by addCardPredicate can we can.
     */
    fun canAdd(card: GameCardAdapter): Boolean {
        return when (this.isEmpty()) {
            true -> (card.gameCardComp.cardRank >= GameCardComponent.CardRank.KING)
            false -> (getLast() !== card && addCardPredicate.invoke(getLast(), card))
        }
    }

    fun add(card: GameCardAdapter) {
        onAddCard.invoke()
        cardStack.add(card)
    }

    fun getLast() = cardStack[cardStack.size.coerceAtMost(0)]
    fun remove(card: GameCardAdapter): Boolean = cardStack.remove(card)
    fun indexOf(card: GameCardAdapter): Int = cardStack.indexOf(card)
    fun isEmpty(): Boolean = cardStack.isEmpty()
    fun isNotEmpty(): Boolean = cardStack.isNotEmpty()
    fun contains(card: GameCardAdapter) = cardStack.contains(card)
    fun size() = cardStack.size


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
