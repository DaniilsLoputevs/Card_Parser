package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport

class CardMoveProcessor(
        private val gameContext: GameContext,
        private val gameViewport: Viewport
) : ScreenInputProcessor {
    private var touchPosition: Vector2 = Vector2(-1f, -1f)
    private var captureOffset: Vector2 = Vector2(-1f, -1f)

    override fun onTouchDown(currentPosition: Vector2) {
        println("TOUCH DOWN  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
        findEntity(currentPosition)
    }

    override fun onTouchDragged(currentPosition: Vector2) {
//        println("TOUCH DRAG  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
        if (gameContext.dndSelectedCard != null) {
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

        gameContext.cards
                .find { it.gameCardComp.isClickable && it.transComp.shape.contains(cursorPosition) }
                ?.let {
                    touchPosition = cursorPosition
                    gameContext.dndSelectedCard = it
                    captureOffset.set(
                            cursorPosition.x - it.transComp.interpolatedPosition.x,
                            cursorPosition.y - it.transComp.interpolatedPosition.y
                    )
                    gameContext.dndEntityStatus = DragAndDropStatus.TOUCH
                }
    }

    private fun moveSelectedTo(currentPosition: Vector2) {
        if (gameContext.dndSelectedCard != null) {
            val newPos = gameViewport.unproject(currentPosition).apply {
                x -= captureOffset.x
                y -= captureOffset.y
            }
            gameContext.dndSelectedCard!!.transComp.setTotalPosition(newPos)
            gameContext.dndEntityStatus = DragAndDropStatus.DRAGGED
        }
    }

    private fun dropSelectedEntity() {
        if (gameContext.dndSelectedCard != null) {
            captureOffset.set(-1f, -1f)
            gameContext.dndEntityStatus = DragAndDropStatus.DROPPED
        }
    }


    enum class DragAndDropStatus {
        NONE,
        TOUCH,
        DRAGGED,
        DROPPED
    }
}