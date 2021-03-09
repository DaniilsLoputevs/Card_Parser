package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2

class DragAndDropManager(val gameContext: GameContext)
    : ScreenInputProcessor {

    override fun onTouchUp(currentPosition: Vector2) {
        println("Drag  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
    }

    override fun onTouchDragged(currentPosition: Vector2) {
        println("Drag  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
    }

    override fun onTouchDown(currentPosition: Vector2) {
        println("UP  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
    }

    enum class DragAndDropStatus {
        NONE,
        TOUCH,
        DRAGGED,
        DROPPED
    }
}