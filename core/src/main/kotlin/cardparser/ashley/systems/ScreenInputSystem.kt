package cardparser.ashley.systems

import cardparser.ashley.systems.ScreenInputSystem.TouchStatus.*
import cardparser.ashley.systems.parts.screeninput.ScreenInputProcessor
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2

/**
 * THIS CODE IS READ ONLY!
 *
 * This system process player input per tick.
 * structure for PI(Player Input) processing, determines the current state of PI (pressed, held, released)
 * and calls the code that should process PI current state(state on current tick).
 *
 * @see inputProcessors : private field
 * @see ScreenInputProcessor
 *
 * @author Daniils Loputevs.
 */
class ScreenInputSystem : EntitySystem() {

    /** Collection of scripts Bundles witch will be triggered when player do any action with screen. */
    lateinit var inputProcessors: Array<ScreenInputProcessor>

    /** current & previous touch/cursor position buffer*/
    private var currPosition: Vector2 = Vector2(-1f, -1f)
    private var prevPosition: Vector2 = Vector2(-1f, -1f)
    private var touchStatus: TouchStatus = NONE


    override fun update(deltaTime: Float) {

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            refreshCurrentPosition()

            if (touchStatus == NONE) { // touchDown
                println("\r\nTOUCH DOWN  screenX = ${currPosition.x}  ##  screenY = ${currPosition.y}")
                inputProcessors.forEach { it.onTouchDown(currPosition) }
                touchStatus = DOWN

            } else if ((touchStatus == DOWN || touchStatus == HOLD)
                    && !isPositionEquals(currPosition, prevPosition)) { // touchDragged
//                println("TOUCH DRAG  screenX = ${currPosition.x}  ##  screenY = ${currPosition.y}")
                inputProcessors.forEach { it.onTouchDragged(currPosition) }
                prevPosition.set(currPosition)
                touchStatus = HOLD
            }

        } else if (touchStatus == DOWN || touchStatus == HOLD) { // onTouchUp
            println("TOUCH UP  screenX = ${currPosition.x}  ##  screenY = ${currPosition.y} \r\n")
            refreshCurrentPosition()

            inputProcessors.forEach { it.onTouchUp(currPosition) }
            prevPosition.set(-1f, -1f)
            touchStatus = NONE
        }

    }


    /* Private part */


    /**
     * Try to make a little bit optimization.
     */
    private fun refreshCurrentPosition() {
        currPosition.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
    }

    /**
     * simple equals for Vector2, it just for a little bit optimization.
     */
    private fun isPositionEquals(one: Vector2, two: Vector2): Boolean {
        return ((one.x == two.x) && (one.y == two.y))
    }

    enum class TouchStatus {
        NONE, DOWN, HOLD
    }

}