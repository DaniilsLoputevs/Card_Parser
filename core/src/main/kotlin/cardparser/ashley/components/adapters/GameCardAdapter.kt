package cardparser.ashley.components.adapters

import cardparser.ashley.components.TouchComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.klondike.GameCardComponent
import cardparser.ashley.toPrint
import com.badlogic.ashley.core.Entity
import ktx.ashley.get

/**
 * For friendly using Entity that is a GameCard
 * * Components never will be null.
 */
data class GameCardAdapter(var entity: Entity = Entity()) {
    val transComp: TransformComponent = entity[TransformComponent.mapper]!!
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
        result = 31 * result + gameCardComp.hashCode()
        result = 31 * result + touchComp.hashCode()
        return result
    }
}
