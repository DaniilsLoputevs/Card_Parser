package towerdefense.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import towerdefense.V_WORLD_HEIGHT_UNITS
import towerdefense.V_WORLD_WIDTH_UNITS


const val IDEAL_SCREEN_ASPECT_RATION : Float = 1.77f

/**
 * Use Vertical Synchronization.
 */
@Deprecated("")
fun cursorPositionToWorldUnits(screenX: Float, screenY: Float, viewport: Viewport): Vector2 {
    val vSyncY = Gdx.graphics.height - screenY
    val rslX: Float = screenX
    val rslY: Float = vSyncY

//    val wuX = (rslX / Gdx.graphics.width) * V_WORLD_WIDTH_UNITS
//    val wuY = (rslY / Gdx.graphics.height) * V_WORLD_HEIGHT_UNITS
//
//    return Vector2(wuX, wuY)
//    return viewport.unproject(Vector2(rslX, rslY))
    return viewport.unproject(Vector2(screenX, screenY))
}