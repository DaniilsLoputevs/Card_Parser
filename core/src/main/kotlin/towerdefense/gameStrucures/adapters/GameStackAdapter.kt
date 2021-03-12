package towerdefense.gameStrucures.adapters

import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import towerdefense.ashley.components.DragAndDropComponent
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.game.GameStackComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.ashley.toPrint

/**
 * For friendly using Entity that is a GameStack
 * * Components never will be null.
 */
data class GameStackAdapter(val entity: Entity) {
    val transComp: TransformComponent = entity[TransformComponent.mapper]!!
    val graphicComp: GraphicComponent = entity[GraphicComponent.mapper]!!
    val gameStackComp: GameStackComponent = entity[GameStackComponent.mapper]!!

    override fun toString(): String {
        return entity.toPrint()
    }
}
