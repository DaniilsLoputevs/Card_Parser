package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2

/**
 * * Has default implementation, cause "not all processors
 * need all methods, will bew good if it MUSN'T override it".
 */
interface ScreenInputProcessor {
    fun onTouchUp(currentPosition: Vector2) {}

    fun onTouchDragged(currentPosition: Vector2) {}

    fun onTouchDown(currentPosition: Vector2) {}
}