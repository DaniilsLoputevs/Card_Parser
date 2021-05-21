package cardparser.ashley.systems

import cardparser.GAME_TITLE
import cardparser.TOUCH_RANGE
import cardparser.event.GameEvent
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import kotlin.math.roundToLong

/** This system process player input per tick add push SIS events */
class ScreenInputSystem(var gameViewport: Viewport) : EntitySystem() {

    /** previous cursorAction */
    private var status: TouchStatus = TouchStatus.NONE

    /** cursor position buffer*/
    private var cursorPos: Vector2 = Vector2(0f, 0f)

    /** track pos of first TOUCH DOWN for check: is next TOUCH UP click OR drag? */
    private var memorizedPos: Vector2 = Vector2(0f, 0f)

    override fun update(deltaTime: Float) {
        refreshCursorPos()
        setTitlePosInfo()
        when {
            /** Press button actions */
            isPressLeftButton() && status == TouchStatus.NONE -> {
                status = TouchStatus.TOUCH
                saveCurPosInMemorizedPos()
            }
            isPressLeftButton() && (status == TouchStatus.GRAB || status == TouchStatus.DRAGGED) -> {
                status = TouchStatus.DRAGGED
                refreshCursorPos()
                pushDragEvent()
            }
            isPressLeftButton() && isMemPosNotZeroAndNotEqualsToCurPos() -> {
                status = TouchStatus.GRAB
                refreshCursorPos()
                pushStartDragEvent()
            }
            /** Stop press button actions */
            isStopPressLeftButton() && status == TouchStatus.DRAGGED -> {
                status = TouchStatus.NONE
                pushDropEvent()
            }
            isStopPressLeftButton() && status == TouchStatus.TOUCH -> {
                status = TouchStatus.NONE
                pushTouchEvent()
                posToZero()
            }
        }
    }

    /* Predicates */
    private fun isPressLeftButton() = Gdx.input.isButtonPressed(Input.Buttons.LEFT)
    private fun isStopPressLeftButton() = !Gdx.input.isButtonPressed(Input.Buttons.LEFT)
    private fun isMemPosNotZeroAndNotEqualsToCurPos() =
            !isPositionEquals(cursorPos, memorizedPos) && !memorizedPos.isZero

    /* events */
    private fun pushDragEvent() {
//        logger.debug("pushDragEvent :: x=${cursorPos.x} & y=${cursorPos.y}")
        GameEventManager.dispatchEvent(GameEvent.DragEvent.apply {
            eventId = GameEventManager.genId()
            cursor = cursorPos
            memorized = memorizedPos
        })
    }

    private fun pushStartDragEvent() {
        GameEventManager.dispatchEvent(GameEvent.StartDragEvent.apply {
            eventId = GameEventManager.genId()
            cursor = cursorPos
            memorized = memorizedPos
        })
    }

    private fun pushTouchEvent() {
//        logger.debug("pushTouchEvent :: x=${cursorPos.x} & y=${cursorPos.y}")
        GameEventManager.dispatchEvent(GameEvent.TouchEvent.apply {
            eventId = GameEventManager.genId()
            position = cursorPos
        })
    }

    private fun pushDropEvent() {
        logger.debug("pushDropEvent :: x=${cursorPos.x} & y=${cursorPos.y}")
        GameEventManager.dispatchEvent(GameEvent.DropEventNew.apply {
            eventId = GameEventManager.genId()
            cursor = cursorPos
        })
    }

    private fun setTitlePosInfo() {
        if (cursorPos.x.isNaN()) return // Avoid invoke NaN.roundToLong() -> NaN Exception
        Gdx.graphics.setTitle("$GAME_TITLE ${cursorPos.x.roundToLong()} # ${cursorPos.y.roundToLong()}")
    }

    /* private methods */
    private fun saveCurPosInMemorizedPos() = memorizedPos.set(cursorPos)
    private fun posToZero() {
        cursorPos.setZero()
        memorizedPos.setZero()
    }

    /** Convert cursor position -> WU position */
    private fun refreshCursorPos() {
        cursorPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        gameViewport.unproject(cursorPos)
    }

    /** simple equals for Vector2, it just for a little bit optimization. */
    private fun isPositionEquals(one: Vector2, two: Vector2): Boolean {
        return (one.x < two.x + TOUCH_RANGE && one.x > two.x - TOUCH_RANGE
                && one.y < two.y + TOUCH_RANGE && one.y > two.y - TOUCH_RANGE)
    }

    /* Inner part */


    companion object {
        private val logger by lazy { loggerApp<ScreenInputSystem>() }
    }

    enum class TouchStatus {
        NONE,
        TOUCH,
        GRAB,
        DRAGGED
    }
}
