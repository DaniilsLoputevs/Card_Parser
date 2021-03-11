package towerdefense.gameStrucures.adapters

import com.badlogic.ashley.core.Entity
import towerdefense.ashley.components.DragAndDropComponent
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.game.GameStackComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.ashley.toPrint

data class GameStackAdapter(val entity: Entity) {
    val transfComp: TransformComponent = entity.findRequiredComponent(TransformComponent.mapper)
    val graphicComp: GraphicComponent = entity.findRequiredComponent(GraphicComponent.mapper)
    val gameStackComp: GameStackComponent = entity.findRequiredComponent(GameStackComponent.mapper)

    override fun toString(): String {
        return entity.toPrint()
    }
}
