package towerdefense.gameStrucures

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import towerdefense.ashley.findRequiredComponent
import towerdefense.gameStrucures.adapters.GameCardAdapter


class DoubleClickProcessor(
        private val gameContext: GameContext,
        private val gameViewport: Viewport
) : ScreenInputProcessor {
    private var touchPosition: Vector2 = Vector2(-1f, -1f)
    private var captureOffset: Vector2 = Vector2(-1f, -1f)

    override fun onTouchDown(currentPosition: Vector2) {
        println("TOUCH DOWN  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
//        findEntity(currentPosition)
    }

    override fun onTouchDragged(currentPosition: Vector2) {
//        println("TOUCH DRAG  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y}")
        if (gameContext.dndSelectedEntity != null) {
//            moveSelectedTo(currentPosition)
        }
    }

    override fun onTouchUp(currentPosition: Vector2) {
        println("TOUCH UP  screenX = ${currentPosition.x}  ##  screenY = ${currentPosition.y} \r\n")
//        dropSelectedEntity()
    }


//    /**
//     * Find Entity by [@param currentPosition] and set it to gameContext.dndSelectedEntity
//     * && change gameContext.dndStatus
//     */
//    private fun findEntity(cursorPosition: Vector2) {
//        // convert cursor position -> WU position
//        gameViewport.unproject(cursorPosition)
//
////        println("DND")
////        var i = 0;
////        println("gameContext.card 0 = ${gameContext.cards[0].getComponent(TransformComponent::class.java).shape}")
////        println("gameContext.card 1 = ${gameContext.cards[1].getComponent(TransformComponent::class.java).shape}")
//        for (eachEntity in gameContext.cards) {
//            val card = GameCardAdapter(eachEntity)
////            println("iter = ${i++}")
//
//            if (!card.gameCardComp.isClickable) continue
//
////            println("follow = $i")
////            val transComp = eachEntity.findRequiredComponent(TransformComponent.mapper)
////            println("DEV")
////            println("transformComp interpolatedPosition=  ${card.transComp.interpolatedPosition}")
////            println("transformComp position=              ${card.transComp.position}")
////            println("transformComp size=                  ${card.transComp.size}")
////            println("transformComp shape=                 ${card.transComp.shape}")
////            println("transformComp prevPosition=          ${card.transComp.prevPosition}")
////            println("DEV")
//
////            println("COORD")
////            println("cursorPosition: x=${cursorPosition.x}  ##  y=${cursorPosition.y}")
////            val wuCoordinates = gameViewport.unproject(cursorPosition)
//
//
//
////            println("WU coordinates: x=${wuCoordinates.x}  ##  y=${wuCoordinates.y}")
////            println("WU coordinates: x=${cursorPosition.x}  ##  y=${cursorPosition.y}")
//
//            val contains = card.transComp.shape.contains(cursorPosition)
////            println("hitbox contains coord point = $contains")
////            println("COORD")
//            if (contains) {
////                println("DND : find entity!!!")
////            if (transformComp.shape.contains(screenY.toFloat(), screenX.toFloat())) {
//                touchPosition = cursorPosition
//                gameContext.dndSelectedEntity = eachEntity
//                captureOffset.set(
//                        cursorPosition.x - card.transComp.interpolatedPosition.x,
//                        cursorPosition.y - card.transComp.interpolatedPosition.y
//                )
//                gameContext.dndEntityStatus = DragAndDropStatus.TOUCH
//                return
//            }
//        }
//
////        println("DND")
//    }
//
//    private fun moveSelectedTo(currentPosition: Vector2) {
////        println("moveSelectedTo()")
//        if (gameContext.dndSelectedEntity != null) {
//            val transComp = gameContext.dndSelectedEntity!!.findRequiredComponent(TransformComponent.mapper)
//            val newPos = gameViewport.unproject(currentPosition).apply {
//                x -= captureOffset.x
//                y -= captureOffset.y
//            }
//            gameContext.dndEntityStatus = DragAndDropStatus.DRAGGED
//            transComp.setTotalPosition(newPos)
//        }
//    }
//
//    private fun dropSelectedEntity() {
////        println("DND M :: dropSelectedEntity()")
//        if (gameContext.dndSelectedEntity != null) {
////            gameContext.dndSelectedEntity = null
//            captureOffset.set(-1f, -1f)
//            gameContext.dndEntityStatus = DragAndDropStatus.DROPPED
//        }
//    }

}