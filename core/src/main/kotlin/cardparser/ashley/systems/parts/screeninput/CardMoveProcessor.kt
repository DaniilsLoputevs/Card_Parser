package cardparser.ashley.systems.parts.screeninput

import cardparser.CARD_STACK_OFFSET
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventManager
import cardparser.gameStrucures.GameRepository
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport

class CardMoveProcessor(
        private val gameViewport: Viewport,
        private val gameRep: GameRepository,
        private val eventManager: GameEventManager
) : ScreenInputProcessor {
    private val captureOffset: Vector2 = Vector2(-1f, -1f)
    private val touchList: MutableList<GameCardAdapter> = mutableListOf()

    override fun onTouchDown(cursorPosition: Vector2) {
        // convert cursor position -> WU position
        gameViewport.unproject(cursorPosition)

        if (!findBindingCardsByPos(cursorPosition)) {
            findUnbindingCardsByPos(cursorPosition)
        }
    }

    override fun onTouchDragged(cursorPosition: Vector2) {
        if (touchList.isEmpty()) return
        // convert cursor position -> WU position
        gameViewport.unproject(cursorPosition)
        draggedTouchList(cursorPosition)
    }

    override fun onTouchUp(cursorPosition: Vector2) {
        if (touchList.isEmpty()) return
        dropTouchList()
        touchList.clear()
    }

    /**
     * Find card by [cursorPosition] in game stacks. It's prefer way to search card,
     * because it's more chance to find card faster then search by iterating all cards.
     *
     * if find -> add it to [touchList]
     */
    private fun findBindingCardsByPos(cursorPosition: Vector2): Boolean {
        return gameRep.findPair(cursorPosition)?.let { pair ->
            val (stack, card) = pair

            stack.gameStackComp.transferCardsToList(card, touchList)
            touchList.forEach { it.transComp.setDepth(it.transComp.getDepth() * 1000) }
            refreshCaptureOffset(cursorPosition, card)
            true
        } ?: false
    }


    /**
     * Find card by [cursorPosition] in all game cards.
     *
     * if find -> add it to gameContext.touchList
     */
    private fun findUnbindingCardsByPos(cursorPosition: Vector2) {
        gameRep.findCard(cursorPosition)?.let { card ->
            refreshCaptureOffset(cursorPosition, card)
            touchList.add(card)
        }
    }

    /** Refresh coordinates of all cards that is in dragged stack if we shift cursor and drag stack. */
    private fun draggedTouchList(currentPosition: Vector2) {
        currentPosition.run {
            x -= captureOffset.x
            y -= captureOffset.y
        }
        touchList.forEach {
            it.transComp.setPosition(currentPosition)
            currentPosition.y -= CARD_STACK_OFFSET
        }
    }

    private fun dropTouchList() {
        eventManager.dispatchEvent(GameEvent.BindingCards.apply {
            this.cards.addAll(touchList)
        })
    }

    /** Calculate a card position with relating to the cursor if we start dragged the card. */
    private fun refreshCaptureOffset(cursorPosition: Vector2, card: GameCardAdapter) {
        captureOffset.set(
                cursorPosition.x - card.transComp.position.x,
                cursorPosition.y - card.transComp.position.y
        )
    }

    enum class TouchStatus {
        NONE,
        TOUCH,
        DRAGGED,
        DROPPED
    }

}
