package towerdefense.ashley.systems.parts.screeninput

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import towerdefense.gameStrucures.GameContext


@Deprecated("Later, not now")
class DoubleClickProcessor(
        private val gameContext: GameContext,
        private val gameViewport: Viewport
) : ScreenInputProcessor {
//    private var touchPosition: Vector2 = Vector2(-1f, -1f)
//    private var captureOffset: Vector2 = Vector2(-1f, -1f)

    override fun onTouchDown(cursorPosition: Vector2) {
    }

    override fun onTouchDragged(cursorPosition: Vector2) {
    }

    override fun onTouchUp(cursorPosition: Vector2) {
    }


}