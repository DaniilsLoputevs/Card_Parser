package cardparser.ashley.objects

import cardparser.ashley.components.GameCardComponent
import cardparser.ashley.components.TouchComponent
import cardparser.ashley.components.TransformComponent
import com.badlogic.ashley.core.Entity
import ktx.ashley.get

/**
 * For friendly using Entity that is a GameCard
 * * Components never will be null.
 */
class Card() : AbstractGameObject() {
    lateinit var transComp: TransformComponent
    lateinit var gameCardComp: GameCardComponent
    lateinit var touchComp: TouchComponent

    fun isTouchable(): Boolean = touchComp.isTouchable
    fun touchable(value: Boolean) = run { touchComp.isTouchable = value }
    fun suit(): GameCardComponent.CardSuit = gameCardComp.cardSuit
    fun rank(): GameCardComponent.CardRank = gameCardComp.cardRank
    fun isOpen(): Boolean = gameCardComp.isCardOpen
    fun open(value: Boolean) = run { gameCardComp.isCardOpen = value }


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
                "${gameCardComp.cardSuit} ${gameCardComp.cardRank} & " +
                "open = ${gameCardComp.isCardOpen} & " +
                "touch = ${touchComp.isTouchable} & " +
                "pos = ${transComp.position} " +
                "}"
    }

}
