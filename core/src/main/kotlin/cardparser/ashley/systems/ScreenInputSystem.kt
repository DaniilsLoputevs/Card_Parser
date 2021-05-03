package cardparser.ashley.systems

import cardparser.DOUBLE_TOUCH_TIME_WINDOW
import cardparser.TOUCH_RANGE
import cardparser.event.GameEvent
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport

/**
 *
 * This system process player input per tick add push 4 types of events
 *
 */
class ScreenInputSystem(
    var gameViewport: Viewport,
    var gameEventManager: GameEventManager
) : EntitySystem() {

    /** previous cursorAction */
    private var status: TouchStatus = TouchStatus.NONE;

    /** cursor position buffer*/
    private var cursorPos: Vector2 = Vector2(0f, 0f)

    /** track pos of first TOUCH DOWN for check: is next TOUCH UP click OR drag? */
    private var memorizedPos: Vector2 = Vector2(0f, 0f)

    /** track time of first TOUCH UP for check: is next TOUCH UP double click? */
    private var touchTime = -1L

    override fun update(deltaTime: Float) {
        refreshCursorPos()
        when {
            /**
             * Press button actions
             */
            isPressLeftButton() && status == TouchStatus.NONE -> {
                status = TouchStatus.TOUCH
                saveCurPosInMemorizedPos()
            }
            isPressLeftButton() && (status == TouchStatus.START_DRAGGED || status == TouchStatus.DRAGGED) -> {
                status = TouchStatus.DRAGGED
                refreshCursorPos()
                pushDragEvent()
            }
            isPressLeftButton() && isMemPosNotZeroAndNotEqualsToCurPos() -> {
                status = TouchStatus.START_DRAGGED
                refreshCursorPos()
                pushStartDragEvent()
            }
            /**
             * Stop press button actions
             */
            isStopPressLeftButton() && status == TouchStatus.DRAGGED -> {
                status = TouchStatus.NONE
                pushDropEvent()
            }
            isStopPressLeftButton() && status == TouchStatus.TOUCH -> {
                status = TouchStatus.NONE
                pushTouchEvent()
                posToZero()
            }
            else -> pushNoneEvent()
        }
    }

    /* Predicates */
    private fun isPressLeftButton() = Gdx.input.isButtonPressed(Input.Buttons.LEFT)
    private fun isMemPosNotZeroAndNotEqualsToCurPos() =
        !isPositionEquals(cursorPos, memorizedPos) && !memorizedPos.isZero

    private fun isStopPressLeftButton() = !Gdx.input.isButtonPressed(Input.Buttons.LEFT)


    /* events */
    private fun saveCurPosInMemorizedPos() {
        memorizedPos.set(cursorPos)
    }

    private fun pushNoneEvent() {
        gameEventManager.dispatchEvent(GameEvent.NoneEvent)
    }

    private fun pushDragEvent() {
//        println("push drag event ${System.currentTimeMillis()}")
        gameEventManager.dispatchEvent(GameEvent.DragEvent.apply {
            cursor = cursorPos
            memorized = memorizedPos
        })
    }

    private fun pushStartDragEvent() {
        gameEventManager.dispatchEvent(GameEvent.StartDragEvent.apply {
            cursor = cursorPos
            memorized = memorizedPos
        })
    }

    private fun pushTouchEvent() {
//        println("push touch event ${System.currentTimeMillis()}")
        gameEventManager.dispatchEvent(GameEvent.TouchEvent.apply {
            position = cursorPos
        })
    }

    private fun pushDropEvent() {
//        println("push drop event ${System.currentTimeMillis()}")
        gameEventManager.dispatchEvent(GameEvent.DropEvent.apply {
            position = cursorPos
        })
    }

    private fun posToZero() {
        cursorPos.setZero()
        memorizedPos.setZero()
    }

    /* Non main part */
    private fun reset() {
        posToZero()
    }

    /**
     *  Convert cursor position -> WU position
     */
    private fun refreshCursorPos() {
        cursorPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        gameViewport.unproject(cursorPos)
    }

    /** simple equals for Vector2, it just for a little bit optimization. */
    private fun isPositionEquals(one: Vector2, two: Vector2): Boolean {
        return (one.x < two.x + TOUCH_RANGE && one.x > two.x - TOUCH_RANGE
                && one.y < two.y + TOUCH_RANGE && one.y > two.y - TOUCH_RANGE)
    }

    enum class TouchStatus {
        NONE,
        TOUCH,
        START_DRAGGED,
        DRAGGED
    }
}

