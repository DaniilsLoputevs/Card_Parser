package towerdefense.stubs

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxInputAdapter
import ktx.ashley.contains
import ktx.ashley.get
import towerdefense.ashley.components.GameCardComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.findComponent

class InProc(
        private val entities: ImmutableArray<Entity>,
        private val gameViewport: Viewport
) : KtxInputAdapter {
    private var selectedEntity : Entity? = null
    private var selected: String? = null

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        println("DOWN  screenX = $screenX  ##  screenY = $screenY  ##  pointer = $pointer")
        findEntity(screenX, screenY)
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        println("UP  screenX = $screenX  ##  screenY = $screenY  ##  pointer = $pointer")
        dropSelectedEntity()
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
//        println("Dragged screenX = $screenX  ##  screenY = $screenY  ##  pointer = $pointer")
        if (selectedEntity != null) {
            moveSelectedTo(screenX, screenY)
        }
        return true
    }


    private fun findEntity(screenX: Int, screenY: Int) {

        for (currEntity in entities) {
//            val gameCardComp = currEntity[GameCardComponent.mapper] ?: continue
            if (!currEntity.contains(GameCardComponent.mapper)) {
                continue
            }
            val transformComp = currEntity.findComponent(TransformComponent.mapper)
            println("DEV")
            println("transformComp interpolatedPosition=  ${transformComp.interpolatedPosition}")
            println("transformComp position=              ${transformComp.position}")
            println("transformComp size=                  ${transformComp.size}")
            println("transformComp shape=                 ${transformComp.shape}")
            println("transformComp prevPosition=          ${transformComp.prevPosition}")
            println("DEV")

            println("findEntity cursor: x=$screenX  ##  y=$screenY")
            val verticalCorrectY = Gdx.graphics.height - screenY
//            val temp = gameViewport.project(Vector3(screenX.toFloat(), verticalCorrectY.toFloat(), 0f))
//            val gameCoordinates = Vector2(temp.x, temp.y)
            val gameCoordinates = gameViewport.project(Vector2(screenX.toFloat(),  verticalCorrectY.toFloat()))
            println("findEntity coord: x=${gameCoordinates.x}  ##  y=${gameCoordinates.y}")
//            println("transformComp.shape.contains(gameCoordinates) = ${transformComp.shape.contains(gameCoordinates)}")
            println("transformComp.shape.contains(screenY.toFloat(), screenX.toFloat()) = ${transformComp.shape.contains(screenY.toFloat(), screenX.toFloat())}")
            if (transformComp.shape.contains(gameCoordinates.x, gameCoordinates.y)) {
//            if (transformComp.shape.contains(screenY.toFloat(), screenX.toFloat())) {
                selected = "selected found"
                selectedEntity = currEntity
//                selectedEntityTransformComp = transformComp
                break
            }
        }
    }

    private fun moveSelectedTo(screenX: Int, screenY: Int) {
        val transformComponent = selectedEntity!!.findComponent(TransformComponent.mapper)
        val verticalCorrectY = Gdx.graphics.height - screenY
        transformComponent.interpolatedPosition.x = screenX.toFloat()
        transformComponent.interpolatedPosition.y = verticalCorrectY.toFloat()
        transformComponent.shape.setPosition(screenX.toFloat(), verticalCorrectY.toFloat())
        transformComponent.position.x = screenX.toFloat()
        transformComponent.position.y = verticalCorrectY.toFloat()
    }

    private fun dropSelectedEntity() {
        println("dropSelectedEntity()")
        selected = null
        selectedEntity = null
    }
}