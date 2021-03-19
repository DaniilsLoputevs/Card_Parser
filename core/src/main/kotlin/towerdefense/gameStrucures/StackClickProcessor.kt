package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport

@Deprecated("Later, not now")
class StackClickProcessor(
        private val gameContext: GameContext,
        private val gameViewport: Viewport
) : ScreenInputProcessor {

    override fun onTouchUp(currentPosition: Vector2) {
        println("TOUCH UP  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y} \r\n")
//        dropSelectedEntity()
    }


}