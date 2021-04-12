package cardparser.ashley.systems.parts.screeninput

import cardparser.CARD_STACK_OFFSET
import cardparser.gameStrucures.GameContext
import cardparser.gameStrucures.adapters.GameCardAdapter
import cardparser.gameStrucures.adapters.GameStackAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport

class CardMoveProcessor(
        private val gameViewport: Viewport,
        private val cards: List<GameCardAdapter>,
        private val stacks: List<GameStackAdapter>
) : ScreenInputProcessor {
    private var captureOffset: Vector2 = Vector2(-1f, -1f)

    override fun onTouchDown(cursorPosition: Vector2) {
        // convert cursor position -> WU position
        gameViewport.unproject(cursorPosition)
        findAndSetStackByPos(cursorPosition)

        // if don't find card in stacks, it could be card without stack
        // TODO - clean it, after cards will be only in stacks.
        if (GameContext.touchListStatus != TouchStatus.TOUCH) {
            println("found card without stack!")
            findCard(cursorPosition)
        }
    }

    override fun onTouchDragged(cursorPosition: Vector2) {
        if (GameContext.touchList.isNotEmpty()) {
            // convert cursor position -> WU position
            gameViewport.unproject(cursorPosition)
            draggedTouchList(cursorPosition)
        }
    }

    override fun onTouchUp(cursorPosition: Vector2) = dropTouchList()

    /**
     * Find card by [cursorPosition] in game stacks. It's prefer way to search card,
     * because it's more optimization way.
     *
     * if find -> add it to gameContext.touchList
     */
    private fun findAndSetStackByPos(cursorPosition: Vector2) {
        stacks.find { it.containsPosInTotalHitBox(cursorPosition) }?.let { stack ->
            stack.gameStackComp.findByPos(cursorPosition)?.let { card ->

                val cardIndex = stack.gameStackComp.indexOf(card)
                stack.gameStackComp.transferElementsFromIndexToList(cardIndex, GameContext.touchList)
                GameContext.touchList.forEach { it.transComp.setDepth(it.transComp.getDepth() * 1000) }
                refreshCaptureOffset(cursorPosition, card)
                GameContext.touchListStatus = TouchStatus.TOUCH
            }
        }
    }

    /**
     * Find card by [cursorPosition] in all game cards.
     *
     * if find -> add it to gameContext.touchList
     */
    private fun findCard(cursorPosition: Vector2) {
        cards.filter { it.touchComp.isTouchable && it.transComp.shape.contains(cursorPosition) }
                .maxByOrNull { it.transComp.position.z }
                ?.let { card ->
                    refreshCaptureOffset(cursorPosition, card)
                    GameContext.touchList.add(card)
                    GameContext.touchListStatus = TouchStatus.TOUCH
                }
    }

    /**
     * Refresh coordinates of all cards that is in dragged stack if we shift cursor and drag stack.
     */
    private fun draggedTouchList(currentPosition: Vector2) {
        currentPosition.run {
            x -= captureOffset.x
            y -= captureOffset.y
        }
        GameContext.touchList.forEach {
            it.transComp.setPosition(currentPosition)
            currentPosition.y -= CARD_STACK_OFFSET
        }
        GameContext.touchListStatus = TouchStatus.DRAGGED
    }

    private fun dropTouchList() {
        GameContext.touchListStatus = TouchStatus.DROPPED
    }

    /**
     * Calculate a card position with relating to the cursor if we start dragged the card.
     */
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
