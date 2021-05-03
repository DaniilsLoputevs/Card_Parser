package cardparser.ashley.components.adapters

import cardparser.ashley.components.GameCardComponent
import cardparser.ashley.components.TouchComponent
import cardparser.ashley.components.TransformComponent
import com.badlogic.ashley.core.Entity
import ktx.ashley.get

/**
 * For friendly using Entity that is a GameCard
 * * Components never will be null.
 */
class GameCardAdapter() : AbstractAdapter() {
    lateinit var transComp: TransformComponent
    lateinit var gameCardComp: GameCardComponent
    lateinit var touchComp: TouchComponent


    /* construction part */


    constructor(entity: Entity) : this() {
        this.entity = entity
    }

    override fun refreshState() {
        this.transComp = entity[TransformComponent.mapper]!!
        this.gameCardComp = entity[GameCardComponent.mapper]!!
        this.touchComp = entity[TouchComponent.mapper]!!
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

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}
