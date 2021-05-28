package cardparser.ashley.systems.input.screen

import cardparser.DOUBLE_TOUCH_TIMER
import cardparser.GAME_TITLE
import cardparser.TOUCH_RANGE
import cardparser.ashley.systems.input.screen.TouchInputStatus.*
import cardparser.common.DeltaTimerDown
import cardparser.logger.LogLevel.*
import cardparser.logger.loggerApp
import cardparser.utils.isContinuePress
import cardparser.utils.isNotContinuePress
import cardparser.utils.isNotPress
import cardparser.utils.isPress
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import kotlin.math.roundToLong

/** NOT DEBUG */
private val logger by lazy { loggerApp<ScreenTouchSystem>().apply { levels(DEV, INFO, WARM, ERROR) } }

interface TouchInputListener {
    val cursor: CursorInfo

    fun listeningActions(): Set<TouchInputStatus>
    fun onAction()
}

enum class TouchInputStatus {
    //    HOLD, // ?? NOT IN USE  -- like DRAG but without change pos
    VIEW,
    GRAB,
    DRAG,
    DROP,
    TOUCH,
    DOUBLE_TOUCH;

}

data class CursorInfo(val grabPos: Vector2, val pos: Vector2, var status: TouchInputStatus)

class ScreenTouchSystem(private var gameViewport: Viewport) : EntitySystem() {
    private val cursor = CursorInfo(Vector2(), Vector2(), VIEW)
    private val prevTickCursorPos = Vector2()
    private val doubleTouchTimer = DeltaTimerDown(DOUBLE_TOUCH_TIMER, isActive = false)

    //    private val viewListeners = mutableSetOf<TouchInputListener>()
    private val grabListeners = mutableSetOf<TouchInputListener>()
    private val dragListeners = mutableSetOf<TouchInputListener>()
    private val dropListeners = mutableSetOf<TouchInputListener>()
    private val touchListeners = mutableSetOf<TouchInputListener>()
    private val doubleTouchListeners = mutableSetOf<TouchInputListener>()

    fun addListeners(listeners: (cursor: CursorInfo) -> Set<TouchInputListener>) {
        listeners.invoke(cursor).forEach { listener ->
            listener.listeningActions().forEach { status ->
                when (status) {
//                    VIEW -> viewListeners.add(listener)
                    GRAB -> grabListeners.add(listener)
                    DRAG -> dragListeners.add(listener)
                    DROP -> dropListeners.add(listener)
                    TOUCH -> touchListeners.add(listener)
                    DOUBLE_TOUCH -> doubleTouchListeners.add(listener)
                }
            }
        }
//        Debug.act("C", "cursor") { logger.debug("cursor", cursor) }
    }

    /** regular deltaTime : 0.0163501 - 0.0175032 */
    // stable v4b - not touch yet - yes drag
    override fun update(deltaTime: Float) {
        updCursorPos()
        doubleTouchTimer.update(deltaTime)
        setTitleNamePosInfo()

        when (cursor.status) {
            VIEW -> {
                /* check: grab or still view */
                if (isPress()) {
                    logger.debug("case - GRAB")
                    cursor.grabPos.set(cursor.pos)
                    notifyListeners(GRAB, grabListeners)
                }
//                else notifyListeners(VIEW, viewListeners)
            }
            GRAB -> {
                /* check: touch or start drag */
                if (isNotPress() && isNotContinuePress() && isCursorInTouchRange(cursor.pos, cursor.grabPos)) {

                    /* check: double touch or touch */
                    if (doubleTouchTimer.isActive) {
                        logger.debug("case - DOUBLE TOUCH")
                        notifyListeners(DOUBLE_TOUCH, doubleTouchListeners)
                        doubleTouchTimer.apply { isActive = false; reset() }
                    } else {
                        logger.debug("case - TOUCH")
                        notifyListeners(TOUCH, touchListeners)
                        doubleTouchTimer.isActive = true
                    }
                    cursor.status = VIEW
                } else if (isContinuePress() && isCursorMove()) {
                    logger.debug("case - DRAG")
                    notifyListeners(DRAG, dragListeners)
                }
//                else logger.debug("case - HOLD")
            }
            DRAG -> {
                /* check: drop or continue dragging */
                if (isNotContinuePress()) {
                    logger.debug("case - DROP")
                    notifyListeners(DROP, dropListeners)
                    cursor.status = VIEW
                } else if (isContinuePress() && isCursorMove()) {
                    logger.debug("case - DRAG")
                    notifyListeners(DRAG, dragListeners)
                }
            }
        }
    }

    /* Inner things */
    private fun notifyListeners(status: TouchInputStatus, listeners: MutableSet<TouchInputListener>) {
        cursor.status = status
        listeners.forEach(TouchInputListener::onAction)
    }

    private fun isCursorMove() = (cursor.pos.x.roundToLong() != cursor.grabPos.x.roundToLong())
            || (cursor.pos.y.roundToLong() != cursor.grabPos.y.roundToLong())


    private fun isCursorInTouchRange(one: Vector2, two: Vector2): Boolean {
        return (one.x < two.x + TOUCH_RANGE && one.x > two.x - TOUCH_RANGE
                && one.y < two.y + TOUCH_RANGE && one.y > two.y - TOUCH_RANGE)
    }

    private fun updCursorPos() {
        prevTickCursorPos.set(cursor.pos)
        cursor.pos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        gameViewport.unproject(cursor.pos)
    }

    private fun setTitleNamePosInfo() {
        if (cursor.pos.x.isNaN()) return // Avoid invoke NaN.roundToLong() -> NaN Exception
        Gdx.graphics.setTitle("$GAME_TITLE ${cursor.pos.x.roundToLong()} # ${cursor.pos.y.roundToLong()}")
    }
}

