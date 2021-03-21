package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import towerdefense.CARD_STACK_OFFSET
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

class CardMoveProcessor(
        private val gameContext: GameContext,
        private val gameViewport: Viewport,
        private val cards: List<GameCardAdapter>,
        private val stacks: List<GameStackAdapter>
) : ScreenInputProcessor {
    private var captureOffset: Vector2 = Vector2(-1f, -1f)

    override fun onTouchDown(cursorPosition: Vector2) {
        // convert cursor position -> WU position
        gameViewport.unproject(cursorPosition)
        findDynamicStackByPos(cursorPosition)

        // if don't find card in stacks, it could be card without stack
        if (gameContext.touchListStatus != TouchStatus.TOUCH) {
            println("findCard = true")
            findCard(cursorPosition)
        }
    }

    override fun onTouchDragged(cursorPosition: Vector2) {
        if (gameContext.touchList.isNotEmpty()) {
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
    private fun findDynamicStackByPos(cursorPosition: Vector2) {
        stacks.find { it.containsPosInTotalHitBox(cursorPosition) }?.let { stack ->
            stack.gameStackComp.findByPos(cursorPosition)?.let { card ->

                val cardIndex = stack.gameStackComp.indexOf(card)
                stack.gameStackComp.removeFromIndexToList(cardIndex, gameContext.touchList)
                gameContext.touchList.forEach { it.transComp.setDepth(it.transComp.getDepth() * 1000) }
                refreshCaptureOffset(cursorPosition, card)
                gameContext.touchListStatus = TouchStatus.TOUCH
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
                    gameContext.touchList.add(card)
                    gameContext.touchListStatus = TouchStatus.TOUCH
                }
    }

    private fun draggedTouchList(currentPosition: Vector2) {
        currentPosition.run {
            x -= captureOffset.x
            y -= captureOffset.y
        }
        gameContext.touchList.forEach {
            it.transComp.setPosition(currentPosition)
            currentPosition.y -= CARD_STACK_OFFSET
        }
        gameContext.touchListStatus = TouchStatus.DRAGGED
    }

    private fun dropTouchList() {
        gameContext.touchListStatus = TouchStatus.DROPPED
    }


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