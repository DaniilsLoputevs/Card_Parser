package cardparser.ashley.systems

import cardparser.DOUBLE_TOUCH_TIME_WINDOW
import cardparser.TOUCH_RANGE
import cardparser.ashley.systems.parts.screeninput.ScreenInputProcessor
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport

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

    /** Composition of Input event processors. It invokes then Input Event happens. */
    lateinit var inputProcessors: Array<ScreenInputProcessor>
    lateinit var gameViewport: Viewport

    /** cursor position buffer*/
    private var cursorPos: Vector2 = Vector2(0f, 0f)

    /** track pos of first TOUCH DOWN for check: is next TOUCH UP click OR drag? */
    private var touchDownPos: Vector2 = Vector2(0f, 0f)

    /** track time of first TOUCH UP for check: is next TOUCH UP double click? */
    private var touchTime = -1L

//    /** TODO - Рефакторинг на**й на Haskell. На when {} б**. */
//    override fun update(deltaTime: Float) {
//        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//            refreshCurrentPosition()
//
//            // TOUCH DOWN
//            if (touchDownPos.isZero) { // touchDown
//                println("\r\nTOUCH DOWN  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
//                touchDownPos.set(cursorPos)
//                inputProcessors.forEach { it.onTouchDown(cursorPos) }
//                println()
//
//                // DRAGGED
//            } else if (!isPositionEquals(cursorPos, touchDownPos) && !touchDownPos.isZero) {
////                println("TOUCH DRAG  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
//                inputProcessors.forEach { it.onTouchDrag(cursorPos) }
////                println()
//            }
//
//            // TOUCH UP || CLICK || DOUBLE CLICK
//        } else if (!cursorPos.isZero) {
//            refreshCurrentPosition()
//            println("TOUCH UP  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
//            inputProcessors.forEach { it.onTouchUp(cursorPos) }
//
//            // CLICK || DOUBLE CLICK
//            if (isPosInTouchRange(cursorPos)) {
//
//                // DOUBLE CLICK
//                if ((System.currentTimeMillis() - touchTime) < DOUBLE_TOUCH_TIME_WINDOW) {
//                    println("DOUBLE CLICK  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
//                    inputProcessors.forEach { it.onDoubleTouch(cursorPos) }
//                    touchTime = -1L
//                    println()
//                }
//
////                println("CLICK  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
////                inputProcessors.forEach { it.onTouch(cursorPos) }
////                touchTime = System.currentTimeMillis()
////                println()
//            }
//            reset()
//            println()
//        }
//    }

    override fun update(deltaTime: Float) {
        when {
            isPressed() -> {
                refreshCurrentPosition()
                when {
                    isTouchDown() -> onTouchDown()
                    isTouchDrag() -> onTouchDrag()
                }
            }
            isTouchUp() -> {
                refreshCurrentPosition()
                onTouchUp()
                when {
                    isClick() -> onTouch()
                    isDoubleClick() -> onDoubleTouch()
                }
                reset()
            }
        }
    }

    /* events */

    private fun onTouchDown() {
        println("\r\nTOUCH DOWN  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
        touchDownPos.set(cursorPos)
        inputProcessors.forEach { it.onTouchDown(cursorPos) }
        println()
    }

    private fun onTouchDrag() {
//        println("TOUCH DRAG  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
        inputProcessors.forEach { it.onTouchDrag(cursorPos) }
//        println()
    }

    private fun onTouchUp() {
        println("TOUCH UP  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
        inputProcessors.forEach { it.onTouchUp(cursorPos) }
    }

    private fun onTouch() {
        println("CLICK  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
        inputProcessors.forEach { it.onTouch(cursorPos) }
        touchTime = System.currentTimeMillis()
        println()
    }

    private fun onDoubleTouch() {
        println("DOUBLE CLICK  screenX = ${cursorPos.x}  ##  screenY = ${cursorPos.y}")
        inputProcessors.forEach { it.onDoubleTouch(cursorPos) }
        touchTime = -1L
        println()
    }

    /* Predicates */

    private fun isPressed() = Gdx.input.isButtonPressed(Input.Buttons.LEFT)

    private fun isTouchDown() = touchDownPos.isZero
    private fun isTouchDrag() = !isPositionEquals(cursorPos, touchDownPos) && !touchDownPos.isZero
    private fun isTouchUp() = !cursorPos.isZero
    private fun isClick() = isPosInTouchRange(cursorPos)
    private fun isDoubleClick() = isPosInTouchRange(cursorPos) && (System.currentTimeMillis() - touchTime) < DOUBLE_TOUCH_TIME_WINDOW

    /* Non main part */

    private fun reset() {
        cursorPos.setZero()
        touchDownPos.setZero()
    }

    private fun refreshCurrentPosition() {
        cursorPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        // convert cursor position -> WU position
        gameViewport.unproject(cursorPos)
    }

    /** simple equals for Vector2, it just for a little bit optimization. */
    private fun isPositionEquals(one: Vector2, two: Vector2): Boolean {
        return ((one.x == two.x) && (one.y == two.y))
    }

    private fun isPosInTouchRange(one: Vector2): Boolean {
        return (touchDownPos.x - TOUCH_RANGE) <= one.x
                && (touchDownPos.x + TOUCH_RANGE) >= one.x
                && (touchDownPos.y - TOUCH_RANGE) <= one.y
                && (touchDownPos.y + TOUCH_RANGE) >= one.y
    }

}