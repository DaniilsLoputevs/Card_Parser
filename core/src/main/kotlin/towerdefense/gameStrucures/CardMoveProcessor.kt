package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import towerdefense.gameStrucures.adapters.GameCardAdapter

class CardMoveProcessor(
        private val gameContext: GameContext,
        private val gameViewport: Viewport,
        private val cards: List<GameCardAdapter>
) : ScreenInputProcessor {
    private var touchPosition: Vector2 = Vector2(-1f, -1f)
    private var captureOffset: Vector2 = Vector2(-1f, -1f)

    override fun onTouchDown(currentPosition: Vector2) {
        println("TOUCH DOWN  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
        findEntity(currentPosition)
    }

    override fun onTouchDragged(currentPosition: Vector2) {
//        println("TOUCH DRAG  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
        if (gameContext.touchingCard != null) {
            moveSelectedTo(currentPosition)
        }
    }

    override fun onTouchUp(currentPosition: Vector2) {
        println("TOUCH UP  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y} \r\n")
        dropSelectedEntity()
    }


    /**
     * Find Entity by [@param currentPosition] and set it to gameContext.dndSelectedEntity
     * && change gameContext.dndStatus
     */
    private fun findEntity(cursorPosition: Vector2) {
        // convert cursor position -> WU position
        gameViewport.unproject(cursorPosition)
        cards
                .filter { it.touchComp.isTouchable && it.transComp.shape.contains(cursorPosition) }
                .maxByOrNull { it.transComp.interpolatedPosition.z }
                ?.let {
                    touchPosition = cursorPosition
                    gameContext.touchingCard = it
                    gameContext.touchingCard!!.transComp.setDepth(1000f)
                    captureOffset.set(
                            cursorPosition.x - it.transComp.interpolatedPosition.x,
                            cursorPosition.y - it.transComp.interpolatedPosition.y
                    )
                    gameContext.touchingCardStatus = DragAndDropStatus.TOUCH
                }
    }

    private fun moveSelectedTo(currentPosition: Vector2) {
        if (gameContext.touchingCard != null) {
            val newPos = gameViewport.unproject(currentPosition).apply {
                x -= captureOffset.x
                y -= captureOffset.y
            }
            gameContext.touchingCard!!.transComp.setTotalPosition(newPos)
            gameContext.touchingCardStatus = DragAndDropStatus.DRAGGED
        }
    }

    private fun dropSelectedEntity() {
        if (gameContext.touchingCard != null) {
            captureOffset.set(-1f, -1f)
            gameContext.touchingCard!!.transComp.setDepth(1f)
            gameContext.touchingCardStatus = DragAndDropStatus.DROPPED
        }
    }


    enum class DragAndDropStatus {
        NONE,
        TOUCH,
        DRAGGED,
        DROPPED
    }
}