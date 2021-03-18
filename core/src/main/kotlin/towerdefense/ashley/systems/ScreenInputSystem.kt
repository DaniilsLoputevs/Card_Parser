package towerdefense.ashley.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import towerdefense.ashley.systems.ScreenInputSystem.TouchStatus.*
import towerdefense.gameStrucures.ScreenInputProcessor

class ScreenInputSystem : EntitySystem() {

    lateinit var inputProcessors: Array<ScreenInputProcessor>

    /** current & previous touch/cursor position */
    private var currPosition: Vector2 = Vector2(-1f, -1f)
    private var prevPosition: Vector2 = Vector2(-1f, -1f)
    private var touchStatus: TouchStatus = NONE


    override fun update(deltaTime: Float) {

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            refreshCurrentPosition()
//            println("SI :: button press")

            if (touchStatus == NONE) { // touchDown
//                println("SI :: touchDown")
                inputProcessors.forEach { it.onTouchDown(currPosition) }
//                prevPosition = currPosition
                touchStatus = DOWN

            } else if ((touchStatus == DOWN || touchStatus == HOLD)
                    && !isPositionEquals(currPosition, prevPosition)) { // touchDragged
//                println("SI :: touchDragged")
                inputProcessors.forEach { it.onTouchDragged(currPosition) }
                prevPosition.set(currPosition)
                touchStatus = HOLD
            }

        } else if (touchStatus == DOWN || touchStatus == HOLD) { // onTouchUp
//            println("SI :: onTouchUp")
            refreshCurrentPosition()

            inputProcessors.forEach { it.onTouchUp(currPosition) }
            // TODO - сделать клик на Stack -> перекинуть карту
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

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
    }

    enum class TouchStatus {
        NONE, DOWN, HOLD
    }

}