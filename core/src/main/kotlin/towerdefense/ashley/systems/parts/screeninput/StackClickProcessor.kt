package towerdefense.ashley.systems.parts.screeninput

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import towerdefense.gameStrucures.GameContext

@Deprecated("Later, not now")
class StackClickProcessor(
        private val gameContext: GameContext,
        private val gameViewport: Viewport
) : ScreenInputProcessor {

    override fun onTouchUp(cursorPosition: Vector2) {
        println("TOUCH UP  screenX = ${cursorPosition.x}  ##  screenY = ${cursorPosition.y} \r\n")
//        dropSelectedEntity()
    }


}