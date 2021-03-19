package towerdefense.ashley.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.DROPPED
import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.NONE
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

class CardBindingSystem : EntitySystem() {
    lateinit var gameContext: GameContext
    lateinit var stacks: List<GameStackAdapter>

    /**
     * just for optimization, only for it.
     * buffer that using like the container for get and hold hitbox center of newCard in:
     * applyCardToNotEmptyStack(...)
     */
    private val newCardCenter = Vector2(-1f, -1f)
    private val cardReturnPos = Vector3(-1f, -1f, -1f)

    /* Methods */

    override fun update(deltaTime: Float) {
        if (gameContext.touchingCard != null && gameContext.touchingCardStatus == DROPPED) {
            processSelectedCard(gameContext.touchingCard!!)
            gameContext.touchingCardStatus = NONE
            gameContext.touchingCard = null
        }
    }

    private fun processSelectedCard(card: GameCardAdapter) {
        println("Binding Sys :: START")
        card.transComp.shape.getCenter(newCardCenter)
        val newCardStack = stacks.find { it.containsPosition(newCardCenter) }

        println("find Stack is = ${newCardStack?.transComp?.interpolatedPosition}")
        println("newCardCenter = $newCardCenter")


        // 1) if we find stack by card pos, it must be NOT NULL, else return card to prev stack
        // 2) avoid the self-adding
        // 3) check: can we add this card to this stack
        if (newCardStack != null && !newCardStack.gameStackComp.contains(card)
                && newCardStack.gameStackComp.canAdd(card)) {

            applyToStack(card, newCardStack)
        } else returnCardToPrevStack(card)

        println("Binding Sys :: END")
    }

    private fun applyToStack(card: GameCardAdapter, newStack: GameStackAdapter) {
        println("APPLY TO STACK")

        /* Getting position for new card before add it into stack */
        val newCardPos = newStack.getNextCardPosition()
        unbindCardFromStack(card)

        newStack.gameStackComp.addGameCard(card)
        card.transComp.setTotalPosition(newCardPos)

        println("APPLY TO STACK END ")
    }

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
                ?.let { card.transComp.setTotalPosition(it.getPositionForCard(card, cardReturnPos)) }

        println("DEV :: returnCardToPrevStack() END")
    }

}
