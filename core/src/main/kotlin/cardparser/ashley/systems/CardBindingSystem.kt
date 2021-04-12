package cardparser.ashley.systems

import cardparser.ashley.systems.parts.screeninput.CardMoveProcessor.TouchStatus.DROPPED
import cardparser.ashley.systems.parts.screeninput.CardMoveProcessor.TouchStatus.NONE
import cardparser.gameStrucures.GameContext
import cardparser.gameStrucures.adapters.GameStackAdapter
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class CardBindingSystem : EntitySystem() {
    lateinit var stacks: List<GameStackAdapter>

    /** buffer for card position. */
    private val cardPositionBuff = Vector3(-1f, -1f, -1f)

    /** buffer for card center coordinates. */
    private val cardCenterBuff = Vector2(-1f, -1f)

    /** buffer for card return position.*/
    private val cardReturnPosBuff = Vector3(-1f, -1f, -1f)

    /* Methods */

    override fun update(deltaTime: Float) {
        if (GameContext.touchList.isNotEmpty() && GameContext.touchListStatus == DROPPED) {
            processTouchList()
            GameContext.touchListStatus = NONE
            GameContext.touchList.clear()
        }
    }

    /**
     * if condition:
     * 1) if we find stack by card pos, it must be NOT NULL.
     * 2) avoid the self-adding.
     * 3) check: can stack add this card.
     * else -> return card to prev stack.
     */
    private fun processTouchList() {
        val firstTouchCard = GameContext.touchList[0]
        firstTouchCard.transComp.shape.getCenter(cardCenterBuff)
        val newCardStack = stacks.find { it.containsPos(cardCenterBuff) }

        if (newCardStack != null && !newCardStack.gameStackComp.contains(firstTouchCard)
                && newCardStack.gameStackComp.canAdd(firstTouchCard)) {
            applyTouchListToStack(newCardStack)
        } else returnTouchListToPrevPosition()
    }

    private fun applyTouchListToStack(newCardStack: GameStackAdapter) {
        val prevStack = stacks.find { it.gameStackComp.contains(GameContext.touchList[0]) }
        GameContext.touchList.forEach {
            prevStack?.gameStackComp?.remove(it)

            /* Getting position for new card before add it into stack */
            newCardStack.getNextCardPosition(cardPositionBuff)
            newCardStack.gameStackComp.add(it)
            it.transComp.setPosition(cardPositionBuff)
        }
    }

    private fun returnTouchListToPrevPosition() {
        stacks.find { it.gameStackComp.contains(GameContext.touchList[0]) }
                ?.let { stack ->
                    GameContext.touchList.forEach { touchCard ->
                        stack.getPosForCard(touchCard, cardReturnPosBuff)
                        touchCard.transComp.setPosition(cardReturnPosBuff)
                    }
                }
    }

}
