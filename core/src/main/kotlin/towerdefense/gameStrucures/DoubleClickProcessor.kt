package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport


@Deprecated("Later, not now")
class DoubleClickProcessor(
        private val gameContext: GameContext,
        private val gameViewport: Viewport
) : ScreenInputProcessor {
//    private var touchPosition: Vector2 = Vector2(-1f, -1f)
//    private var captureOffset: Vector2 = Vector2(-1f, -1f)

    override fun onTouchDown(currentPosition: Vector2) {
        println("TOUCH DOWN  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
//        findEntity(currentPosition)
    }

    override fun onTouchDragged(currentPosition: Vector2) {
//        println("TOUCH DRAG  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
        if (gameContext.dndSelectedCard != null) {
//            moveSelectedTo(currentPosition)
        }
    }

    override fun onTouchUp(currentPosition: Vector2) {
        println("TOUCH UP  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y} \r\n")
//        dropSelectedEntity()
    }


}