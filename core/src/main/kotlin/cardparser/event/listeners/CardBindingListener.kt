package cardparser.event.listeners

import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.gameStrucures.GameRepository
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class CardBindingListener(
        private val gameRep: GameRepository
) : GameEventListener {

    /** buffer for card position. */
    private val cardPositionBuff = Vector3(-1f, -1f, -1f)

    /** buffer for card center coordinates. */
    private val cardCenterBuff = Vector2(-1f, -1f)

    /** buffer for card return position.*/
    private val cardReturnPosBuff = Vector3(-1f, -1f, -1f)


    /**
     * if condition:
     * 1) if we find stack by card pos, it must be NOT NULL.
     * 2) avoid the self-adding.
     * 3) check: can stack add this card.
     * else -> return card to prev stack.
     */
    override fun onEvent(event: GameEvent) {
        event as GameEvent.BindingCards

        val firstCard = event.cards.first()
        firstCard.transComp.shape.getCenter(cardCenterBuff)
        val newStack = gameRep.findStack(cardCenterBuff)

        if (newStack != null && !newStack.gameStackComp.contains(firstCard) 
                && newStack.gameStackComp.canAdd(firstCard)) {
            applyToStack(event.cards, newStack, firstCard)
        } else returnToPrevPosition(event.cards)
        event.cards.clear()
    }


    private fun applyToStack(cards: MutableList<GameCardAdapter>, newCardStack: GameStackAdapter,
                             firstCard : GameCardAdapter) {
        val prevStack = gameRep.findStack(firstCard)
        cards.forEach {
            prevStack?.gameStackComp?.remove(it)

            /* Getting position for new card before add it into stack */
            newCardStack.getNextCardPosition(cardPositionBuff)
            newCardStack.gameStackComp.add(it)
            it.transComp.setPosition(cardPositionBuff)
        }
    }

    private fun returnToPrevPosition(cards: MutableList<GameCardAdapter>) {
        gameRep.findStack(cards.first())?.let { stack ->
            cards.forEach { touchCard ->
                stack.getPosForCard(touchCard, cardReturnPosBuff)
                touchCard.transComp.setPosition(cardReturnPosBuff)
            }
        }
    }
}