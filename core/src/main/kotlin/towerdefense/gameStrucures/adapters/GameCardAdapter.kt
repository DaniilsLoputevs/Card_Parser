package towerdefense.gameStrucures.adapters

import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import towerdefense.ashley.components.DragAndDropComponent
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.ashley.toPrint

/**
 * For friendly using Entity that is a GameCard
 * * Components never will be null.
 */
data class GameCardAdapter(val entity: Entity) {
    val transComp: TransformComponent = entity[TransformComponent.mapper]!!
    val graphicComp: GraphicComponent = entity[GraphicComponent.mapper]!!
    val gameCardComp: GameCardComponent = entity[GameCardComponent.mapper]!!
    val DNDComp: DragAndDropComponent = entity[DragAndDropComponent.mapper]!!

    override fun toString(): String {
        return entity.toPrint()
    }
}
