package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.findRequiredComponent

class DragAndDropManager(
        private val gameContext: GameContext,
        private val gameViewport: Viewport
) : ScreenInputProcessor {
    private var touchPosition: Vector2 = Vector2(-1f, -1f)
    private var captureOffset: Vector2 = Vector2(-1f, -1f)

    override fun onTouchDown(currentPosition: Vector2) {
        println("DOWN  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
        findEntity(currentPosition)
    }

    override fun onTouchDragged(currentPosition: Vector2) {
//        println("DRAG  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
        if (gameContext.dndSelectedEntity != null) {
            moveSelectedTo(currentPosition)
        }
    }

    override fun onTouchUp(currentPosition: Vector2) {
        println("UP  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
        dropSelectedEntity()
    }


    /**
     * Find Entity by [@param currentPosition] and set it to gameContext.dndSelectedEntity
     * && change gameContext.dndStatus
     */
    private fun findEntity(currentPosition: Vector2) {
        for (currEntity in gameContext.cards) {

            if (!currEntity.findRequiredComponent(GameCardComponent.mapper).isClickable) {
                continue
            }
            val transComp = currEntity.findRequiredComponent(TransformComponent.mapper)
//            println("DEV")
//            println("transformComp interpolatedPosition=  ${transmComp.interpolatedPosition}")
//            println("transformComp position=              ${transmComp.position}")
//            println("transformComp size=                  ${transmComp.size}")
//            println("transformComp shape=                 ${transmComp.shape}")
//            println("transformComp prevPosition=          ${transmComp.prevPosition}")
//            println("DEV")

//            println("COORD")
//            println("findEntity cursor: x=$screenX  ##  y=$screenY")
//            val wuCoordinates = cursorPositionToWorldUnits(screenX.toFloat(), screenY.toFloat(),viewport)
            val wuCoordinates = gameViewport.unproject(currentPosition)
//            println("findEntity coord:  x=${wuCoordinates.x}  ##  y=${wuCoordinates.y}")

            val contains = transComp.shape.contains(wuCoordinates)
//            println("hitbox contains coord point = $contains")
//            println("COORD")
            if (contains) {
                println("DND : find entity!!!")
//            if (transformComp.shape.contains(screenY.toFloat(), screenX.toFloat())) {
                touchPosition = currentPosition
                gameContext.dndSelectedEntity = currEntity
                captureOffset.set(
                        wuCoordinates.x - transComp.interpolatedPosition.x,
                        wuCoordinates.y - transComp.interpolatedPosition.y
                )
                gameContext.dndEntityStatus = DragAndDropStatus.TOUCH
                return
            }
        }
    }
    private fun moveSelectedTo(currentPosition: Vector2) {
//        println("moveSelectedTo()")
        if (gameContext.dndSelectedEntity != null) {
            val transformComponent = gameContext.dndSelectedEntity!!.findRequiredComponent(TransformComponent.mapper)
            val newEntityPosition = gameViewport.unproject(currentPosition).apply {
                x -= captureOffset.x
                y -= captureOffset.y
            }
            gameContext.dndEntityStatus = DragAndDropStatus.DRAGGED
            transformComponent.setTotalPosition(newEntityPosition)
        }
    }

    private fun dropSelectedEntity() {
//        println("DND M :: dropSelectedEntity()")
        if (gameContext.dndSelectedEntity != null) {
//            gameContext.dndSelectedEntity = null
            captureOffset.set(-1f, -1f)
            gameContext.dndEntityStatus = DragAndDropStatus.DROPPED
        }
    }


    enum class DragAndDropStatus {
        NONE,
        TOUCH,
        DRAGGED,
        DROPPED,
//        BINDING_SUCCESS,
//        BINDING_FAIL
    }
}