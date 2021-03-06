package towerdefense.stubs

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxInputAdapter
import ktx.ashley.contains
import towerdefense.ashley.components.GameCardComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.cursorPositionToWorldUnits
import towerdefense.findComponent

class InProc(
        private val entities: ImmutableArray<Entity>,
        private val gameViewport: Viewport
) : KtxInputAdapter {
    private var selectedEntity: Entity? = null
    private var captureOffset: Vector2? = null



    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        println()
        println("DOWN  screenX = $screenX  ##  screenY = $screenY  ##  pointer = $pointer  ##  button = $button")
        findEntity(screenX, screenY)
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        println("UP  screenX = $screenX  ##  screenY = $screenY  ##  pointer = $pointer  ##  button = $button")
        dropSelectedEntity()
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
//        println("Dragged screenX = $screenX  ##  screenY = $screenY  ##  pointer = $pointer")
        if (selectedEntity != null) {
            moveSelectedTo(screenX.toFloat(), screenY.toFloat())
        }
        return true
    }


    private fun findEntity(screenX: Int, screenY: Int) {

        for (currEntity in entities) {
//            val gameCardComp = currEntity[GameCardComponent.mapper] ?: continue
            if (!currEntity.contains(GameCardComponent.mapper)) {
                continue
            }
            val transmComp = currEntity.findComponent(TransformComponent.mapper)
//            println("DEV")
//            println("transformComp interpolatedPosition=  ${transmComp.interpolatedPosition}")
//            println("transformComp position=              ${transmComp.position}")
//            println("transformComp size=                  ${transmComp.size}")
//            println("transformComp shape=                 ${transmComp.shape}")
//            println("transformComp prevPosition=          ${transmComp.prevPosition}")
            println("DEV")

//            println("COORD")
//            println("findEntity cursor: x=$screenX  ##  y=$screenY")
            val wuCoordinates = cursorPositionToWorldUnits(screenX.toFloat(), screenY.toFloat(), gameViewport)
//            println("findEntity coord:  x=${wuCoordinates.x}  ##  y=${wuCoordinates.y}")

            val contains = transmComp.shape.contains(wuCoordinates)
//            println("hitbox contains coord point = $contains")
//            println("COORD")
            if (contains) {
//            if (transformComp.shape.contains(screenY.toFloat(), screenX.toFloat())) {
                selectedEntity = currEntity
                captureOffset = findCaptureOffset(transmComp, wuCoordinates)
                break
            }
        }
    }

    private fun moveSelectedTo(screenX: Float, screenY: Float) {
        val transformComponent = selectedEntity!!.findComponent(TransformComponent.mapper)
        val newPositionCoordinates = cursorPositionToWorldUnits(screenX, screenY, gameViewport).apply {
            x -= captureOffset!!.x
            y -= captureOffset!!.y
        }

        transformComponent.setTotalPosition(newPositionCoordinates)
    }

    private fun dropSelectedEntity() {
        println("dropSelectedEntity()")
        selectedEntity = null
        captureOffset = null
    }

    private fun findCaptureOffset(transComp: TransformComponent, position: Vector2) : Vector2 {
        val offsetX : Float = position.x - transComp.interpolatedPosition.x
        val offsetY : Float = position.y - transComp.interpolatedPosition.y

        return Vector2(offsetX, offsetY)
    }


}