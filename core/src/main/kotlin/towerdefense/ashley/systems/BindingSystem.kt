package towerdefense.ashley.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.RemoveComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameStacksComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.event.GameEventManager
import towerdefense.gameStrucures.DragAndDropManager.DragAndDropStatus.DROPPED
import towerdefense.gameStrucures.DragAndDropManager.DragAndDropStatus.NONE
import towerdefense.gameStrucures.GameContext

class BindingSystem(
) : IteratingSystem(allOf(GameCardComponent::class).exclude(RemoveComponent::class.java).get()) {
    lateinit var gameContext: GameContext

    override fun update(deltaTime: Float) {
        if (gameContext.dndSelectedEntity != null
                && gameContext.dndEntityStatus == DROPPED) {
//            processEntity(gameContext.dndSelectedEntity!!, deltaTime)
            gameContext.dndEntityStatus = NONE
            gameContext.dndSelectedEntity = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
//        val entityTransComp = entity.findRequiredComponent(TransformComponent.mapper)
//        for (stack in gameContext.stacks) {
//            val stackComp = stack.findRequiredComponent(GameStacksComponent.mapper)
//            val lastCard = stackComp.getLastCard()
//            val lastCardTransComp = lastCard.findRequiredComponent(TransformComponent.mapper)
//            if (lastCardTransComp.shape.contains(entityTransComp.shape.getCenter(Vector2()))) {
//                // TODO - see AttachSystem in DarkMatter
//            }
//        }
    }


    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
    }

}
