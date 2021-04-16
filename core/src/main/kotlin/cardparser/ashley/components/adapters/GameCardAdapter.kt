package cardparser.ashley.components.adapters

import cardparser.ashley.components.TouchComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.klondike.GameCardComponent
import com.badlogic.ashley.core.Entity
import ktx.ashley.get

/**
 * For friendly using Entity that is a GameCard
 * * Components never will be null.
 */
data class GameCardAdapter(val entity: Entity) {
    val transComp: TransformComponent = entity[TransformComponent.mapper]!!
    val gameCardComp: GameCardComponent = entity[GameCardComponent.mapper]!!
    val touchComp: TouchComponent = entity[TouchComponent.mapper]!!


    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun toString(): String {
        return "card={ " +
                "${gameCardComp.cardSuit} ${gameCardComp.cardRank} " +
                "open = ${gameCardComp.isCardOpen} " +
                "touch = ${touchComp.isTouchable} " +
                "pos = ${transComp.position} " +
                "}"
    }

    override fun hashCode(): Int {
        var result = entity.hashCode()
        result = 31 * result + transComp.hashCode()
        result = 31 * result + gameCardComp.hashCode()
        result = 31 * result + touchComp.hashCode()
        return result
    }
}
