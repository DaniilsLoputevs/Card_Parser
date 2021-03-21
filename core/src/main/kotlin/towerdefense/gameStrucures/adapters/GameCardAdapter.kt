package towerdefense.gameStrucures.adapters

import com.badlogic.ashley.core.Entity
import ktx.ashley.get
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TouchComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.toPrint

/**
 * For friendly using Entity that is a GameCard
 * * Components never will be null.
 */
data class GameCardAdapter(val entity: Entity) {
    val transComp: TransformComponent = entity[TransformComponent.mapper]!!
    val graphicComp: GraphicComponent = entity[GraphicComponent.mapper]!!
    val gameCardComp: GameCardComponent = entity[GameCardComponent.mapper]!!
    val touchComp: TouchComponent = entity[TouchComponent.mapper]!!


    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun toString(): String {
        return entity.toPrint()
    }

    override fun hashCode(): Int {
        var result = entity.hashCode()
        result = 31 * result + transComp.hashCode()
        result = 31 * result + graphicComp.hashCode()
        result = 31 * result + gameCardComp.hashCode()
        result = 31 * result + touchComp.hashCode()
        return result
    }
}
