package towerdefense.ashley.components.game

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import towerdefense.gameStrucures.adapters.GameCardAdapter

class GameStackComponent : Component, Pool.Poolable {
    var cardStack: MutableList<GameCardAdapter> = mutableListOf()

    @Deprecated("later, not now")
    var onClick: () -> Unit = {}
    var onAddCard: () -> Unit = {}
    var addCardPredicate: (GameCardAdapter, GameCardAdapter) -> Boolean = { _: GameCardAdapter, _: GameCardAdapter -> true }

    fun isEmpty() = cardStack.isEmpty()

    /**
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
            false -> (getLastCard() !== card && addCardPredicate.invoke(getLastCard(), card))
        }
    }

    fun addGameCard(card: GameCardAdapter) {
        onAddCard.invoke()
        cardStack.add(card)
    }

    fun getLastCard() = cardStack[cardStack.size.coerceAtMost(0)]

    /**
     * TODO - make cascade remove from {@param card} index in stack
     */
    fun removeGameCard(card: GameCardAdapter): Boolean = cardStack.remove(card)
    fun indexOfCard(card: GameCardAdapter): Int = cardStack.indexOf(card)

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