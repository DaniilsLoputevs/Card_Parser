package cardparser.ashley.systems.parts.screeninput

import com.badlogic.gdx.math.Vector2

/**
 * Contract for [ScreenInputSystem] parts.
 * Implementation of this interface must process player input and do business logic.
 *
 * API will be invoked in System, then player do this action.
 * Player press on screen            => invoke onTouchUp()
 * Player dragging on screen         => invoke onTouchDragged()
 * Player finish dragging on screen  => invoke onTouchDown()
 *
 * Also, it can be understand like a script bundle witch will triggered when player do any action with screen.
 *
 * * Has default implementation, cause "not all processors
 * need all methods, will bew good if it MUSN'T override it".
 */
interface ScreenInputProcessor {
    fun onTouchDown(cursorPosition: Vector2) {}

    fun onTouchDragged(cursorPosition: Vector2) {}

    fun onTouchUp(cursorPosition: Vector2) {}

    /** TODO - onClick() form js - keyUp */
    fun onTouch(cursorPosition: Vector2) {}

    fun onDoubleClick(cursorPosition: Vector2) {}


}