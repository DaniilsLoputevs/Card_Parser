package towerdefense.ashley.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import towerdefense.ashley.components.DragAndDropComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.RemoveComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.systems.ScreenInputSystem.TouchStatus.*
import towerdefense.gameStrucures.ScreenInputProcessor

class ScreenInputSystem
    : IteratingSystem(allOf(TransformComponent::class, DragAndDropComponent::class, GameCardComponent::class)
        .exclude(RemoveComponent::class.java).get()) {

   lateinit var inputProcessors: Array<ScreenInputProcessor>
    /** current & previous touch/cursor position */
    private var currPosition: Vector2 = Vector2(-1f, -1f)
    private var prevPosition: Vector2 = Vector2(-1f, -1f)
    private var touchStatus : TouchStatus = NONE



    override fun update(deltaTime: Float) {

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            refreshCurrentPosition()
//            println("SI :: button press")
//            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
//                println("SI DEBUG")
//                println("prevPosition = $prevPosition")
//                println("currPosition = $currPosition")
//                println("currPosition = $touchStatus")
//                println("dragged cond = ${(touchStatus == DOWN || touchStatus == HOLD)
//                        && !isPositionEquals(currPosition, prevPosition)}")
//                println("SI DEBUG")
//            }

//            if (prevPosition.x == -1f) { // touchDown
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
            prevPosition.set(-1f, -1f)
            touchStatus = NONE
        }

//        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
//            println("SI DEBUG")
//            println("prevPosition = ${prevPosition}")
//            println("currPosition = ${currPosition}")
//            println("SI DEBUG")
//        }
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        TODO("STUB :: Maybe we don't need to implement this method")
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