package towerdefense.ashley.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import towerdefense.CARD_STACK_OFFSET
import towerdefense.ashley.components.RemoveComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.DROPPED
import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.NONE
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

class CardBindingSystem
    : IteratingSystem(allOf(GameCardComponent::class).exclude(RemoveComponent::class.java).get()) {
    lateinit var gameContext: GameContext

    /**
     * just for optimization, only for it.
     * buffer that using like the container for get and hold hitbox center of newCard in:
     * applyCardToNotEmptyStack(...)
     */
    private val newCardCenter = Vector2(-1f, -1f)

    /* Methods */

    override fun update(deltaTime: Float) {
        if (gameContext.touchingCard != null
                && gameContext.touchingCardStatus == DROPPED) {
            processSelectedCard(gameContext.touchingCard!!)
            gameContext.touchingCardStatus = NONE
            gameContext.touchingCard = null
        }
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        TODO("Not yet implemented")
    }

    private fun processSelectedCard(card: GameCardAdapter) {
        var cardApplyIntoNewStack = false
        for (stack in gameContext.stacks) {
            cardApplyIntoNewStack = when (stack.gameStackComp.isEmpty()) {
                true -> applyCardToEmptyStack(card, stack)
                false -> applyCardToNotEmptyStack(card, stack)
            }
            if (cardApplyIntoNewStack) break
        }
        if (!cardApplyIntoNewStack) returnGameCardToStack(card)
    }

    private fun applyCardToEmptyStack(card: GameCardAdapter, stack: GameStackAdapter): Boolean {
        return if (stack.transComp.shape.contains(card.transComp.shape.getCenter(newCardCenter))) {
            unbindCardFromStack(card)
            stack.gameStackComp.addGameCard(card)

            val newPos = Vector2(
                    stack.transComp.interpolatedPosition.x,
                    stack.transComp.interpolatedPosition.y,
            )
            card.transComp.setTotalPosition(newPos)
            card.gameCardComp.moveNextCards(newPos)
            true
        } else false
    }

    private fun applyCardToNotEmptyStack(card: GameCardAdapter, stack: GameStackAdapter): Boolean {
        val stackLastCard = stack.gameStackComp.getLastCard()
        card.transComp.shape.getCenter(newCardCenter)

        /* check: is it try to add card to current stack.
        we can't add card to stack, what already contains this card. */
        if (card == stackLastCard) return false

        if (stackLastCard.transComp.shape.contains(newCardCenter)) {

            /* check: can we add card to current stack(stack last card) */
            if (!stackLastCard.gameCardComp.setNextPredicate.test(card.gameCardComp)) return false

            unbindCardFromStack(card)
            stackLastCard.gameCardComp.next = card
            stack.gameStackComp.addGameCard(card)

            val newPos = Vector2(
                    stackLastCard.transComp.interpolatedPosition.x,
                    stackLastCard.transComp.interpolatedPosition.y - CARD_STACK_OFFSET,
            )
            card.transComp.setTotalPosition(newPos)
            card.gameCardComp.moveNextCards(newPos)
            return true
        } else return false
    }

    /**
     * Unbinding card from it's stack.
     */
    private fun unbindCardFromStack(card: GameCardAdapter) {
        gameContext.stacks
                .find { it.gameStackComp.contains(card) }
                ?.let { it.gameStackComp.removeGameCard(card) }
    }

    private fun returnGameCardToStack(card: GameCardAdapter) {
        gameContext.stacks
                .find { it.gameStackComp.contains(card) }
                ?.let {
                    val offsetY = it.gameStackComp.size() + CARD_STACK_OFFSET
                    val newPos = Vector2(
                            it.transComp.interpolatedPosition.x,
                            it.transComp.interpolatedPosition.y - offsetY,
                    )
                    card.apply {
                        transComp.setTotalPosition(newPos)
                        gameCardComp.moveNextCards(newPos)
                    }
                }
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
    }

}
