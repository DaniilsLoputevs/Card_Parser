package towerdefense.ashley.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.Vector2
import towerdefense.CARD_STACK_OFFSET
import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.DROPPED
import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.NONE
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

/** TODO - Переписать почти всё на хуй. */
class CardBindingSystem : EntitySystem() {
    lateinit var gameContext: GameContext
    lateinit var stacks: List<GameStackAdapter>

    /**
     * just for optimization, only for it.
     * buffer that using like the container for get and hold hitbox center of newCard in:
     * applyCardToNotEmptyStack(...)
     */
    private val newCardCenter = Vector2(-1f, -1f)
    private val cardReturnPos = Vector2(-1f, -1f)

    /* Methods */

    override fun update(deltaTime: Float) {
        if (gameContext.touchingCard != null && gameContext.touchingCardStatus == DROPPED) {
            processSelectedCard(gameContext.touchingCard!!)
            gameContext.touchingCardStatus = NONE
            gameContext.touchingCard = null
        }
    }

    //    @Deprecated("hard understanding logic, need to rewrite")
//    private fun processSelectedCard(card: GameCardAdapter) {
//        var cardApplyIntoNewStack = false
//        for (stack in stacks) {
//            cardApplyIntoNewStack = when (stack.gameStackComp.isEmpty()) {
//                true -> applyCardToEmptyStack(card, stack)
//                false -> applyCardToNotEmptyStack(card, stack)
//            }
//            if (cardApplyIntoNewStack) break
//        }
//        if (!cardApplyIntoNewStack) returnGameCardToStack(card)
//    }
    @Deprecated("hard understanding logic, need to rewrite")
    private fun processSelectedCard(card: GameCardAdapter) {
        println("Binding Sys :: START")
        card.transComp.shape.getCenter(newCardCenter)
        val newCardStack = stacks.find { it.containsPosition(newCardCenter) }

        println("find Stack is = ${newCardStack?.transComp?.interpolatedPosition}")
        println("newCardCenter = $newCardCenter")

        /* if we find stack by card pos, it must be NOT NULL, else return card to prev stack */
        /* avoid the self-adding */
        if (newCardStack != null && !newCardStack.gameStackComp.contains(card)) {
            applyToStack(card, newCardStack)
        } else returnCardToPrevStack(card)

        println("Binding Sys :: END")
    }

    private fun applyToStack(card: GameCardAdapter, newStack: GameStackAdapter) {
        println("APPLY TO STACK")
        println("APPLY canAdd(...) = ${!newStack.gameStackComp.canAdd(card)}")
        if (!newStack.gameStackComp.canAdd(card)) return

        /* Getting position for new card before add it into stack */
        val newCardPos = newStack.getNextCardPosition()
        unbindCardFromStack(card)

        newStack.gameStackComp.addGameCard(card)
        card.transComp.setTotalPosition(newCardPos)
        println("APPLY TO STACK END ")
//        card.gameCardComp.moveNextCards(newPos)
    }

    private fun applyToEmptyStackNEW(card: GameCardAdapter, newStack: GameStackAdapter): Boolean {
//        val stackLastCard = newStack.gameStackComp.getLastCard()

        /* check: can we add card to current stack(stack last card)
       * TODO - rewrite to Stack Add Predicate */
//        if (!stackLastCard.gameCardComp.setNextPredicate.test(card.gameCardComp)) return false

        unbindCardFromStack(card)
//        stackLastCard.gameCardComp.next = card
        newStack.gameStackComp.addGameCard(card)

        val newPos = Vector2(
                newStack.transComp.interpolatedPosition.x,
                newStack.transComp.interpolatedPosition.y
        )
        card.transComp.setTotalPosition(newPos)
        card.gameCardComp.moveNextCards(newPos)
        return true
    }


    private fun applyToNotEmptyStackNEW(card: GameCardAdapter, newStack: GameStackAdapter): Boolean {
        val stackLastCard = newStack.gameStackComp.getLastCard()
        /* check: is it try to add card to current stack.
        we can't add card to stack, what already contains this card. */
        if (card == stackLastCard) return false

        /* check: can we add card to current stack(stack last card)
        * TODO - rewrite to Stack Add Predicate */
        if (!stackLastCard.gameCardComp.setNextPredicate.test(card.gameCardComp)) return false

        unbindCardFromStack(card)
//        stackLastCard.gameCardComp.next = card
        newStack.gameStackComp.addGameCard(card)

        val newPos = Vector2(
                stackLastCard.transComp.interpolatedPosition.x,
                stackLastCard.transComp.interpolatedPosition.y -
                        newStack.gameStackComp.size() * CARD_STACK_OFFSET
        )
        card.transComp.setTotalPosition(newPos)
        card.gameCardComp.moveNextCards(newPos)
        return true
    }


//    private fun applyCardToEmptyStack(card: GameCardAdapter, stack: GameStackAdapter): Boolean {
//        return if (stack.transComp.shape.contains(card.transComp.shape.getCenter(newCardCenter))) {
//            unbindCardFromStack(card)
//            stack.gameStackComp.addGameCard(card)
//
//            val newPos = Vector2(
//                    stack.transComp.interpolatedPosition.x,
//                    stack.transComp.interpolatedPosition.y,
//            )
//            card.transComp.setTotalPosition(newPos)
//            card.gameCardComp.moveNextCards(newPos)
//            true
//        } else false
//    }
//
//    private fun applyCardToNotEmptyStack(card: GameCardAdapter, stack: GameStackAdapter): Boolean {
//        val stackLastCard = stack.gameStackComp.getLastCard()
//        card.transComp.shape.getCenter(newCardCenter)
//
//        /* check: is it try to add card to current stack.
//        we can't add card to stack, what already contains this card. */
//        if (card == stackLastCard) return false
//
//        if (stackLastCard.transComp.shape.contains(newCardCenter)) {
//
//            /* check: can we add card to current stack(stack last card) */
//            if (!stackLastCard.gameCardComp.setNextPredicate.test(card.gameCardComp)) return false
//
//            unbindCardFromStack(card)
//            stackLastCard.gameCardComp.next = card
//            stack.gameStackComp.addGameCard(card)
//
//            val newPos = Vector2(
//                    stackLastCard.transComp.interpolatedPosition.x,
//                    stackLastCard.transComp.interpolatedPosition.y -
//                            stack.gameStackComp.size() * CARD_STACK_OFFSET
//            )
//            card.transComp.setTotalPosition(newPos)
//            card.gameCardComp.moveNextCards(newPos)
//            return true
//        } else return false
//    }

    /**
     * Unbinding card from it's stack.
     */
    private fun unbindCardFromStack(card: GameCardAdapter) {
        stacks.find { it.gameStackComp.contains(card) }
                ?.let { it.gameStackComp.removeGameCard(card) }
    }

    private fun returnCardToPrevStack(card: GameCardAdapter) {
        println("DEV :: returnCardToPrevStack() START")

        stacks.find { it.gameStackComp.contains(card) }
                ?.let {
                    val newPos = it.getPosForCard(card, cardReturnPos)
                    card.apply {
                        transComp.setTotalPosition(newPos)
//                        gameCardComp.moveNextCards(newPos)
                    }
                }

        println("DEV :: returnCardToPrevStack() END")
    }

}
