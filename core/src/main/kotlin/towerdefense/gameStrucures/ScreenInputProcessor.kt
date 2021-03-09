package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2

interface ScreenInputProcessor {
    fun onTouchUp(currentPosition: Vector2)

    fun onTouchDragged(currentPosition: Vector2)

    fun onTouchDown(currentPosition: Vector2)
}