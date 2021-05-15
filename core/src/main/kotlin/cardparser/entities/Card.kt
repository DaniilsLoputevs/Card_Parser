package cardparser.entities

import cardparser.ashley.components.*
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import ktx.ashley.allOf

/**
 * For friendly using Entity that is a GameCard
 * * Components never will be null.
 */
class Card() : CardAPI, TouchAPI, GameEntity() {
    override lateinit var transComp: TransformComp
    override lateinit var cardComp: CardComp
    override lateinit var touchComp: TouchComp


    /* inner part */


    constructor(entity: Entity) : this() {
        this.entity = entity
    }

    override fun toString(): String {
        return "card={ " +
                "${cardComp.cardSuit} ${cardComp.cardRank} & " +
                "open = ${cardComp.isCardOpen} & " +
                "touch = ${touchComp.isTouchable} & " +
                "pos = ${transComp.position} " +
                "}"
    }

    val GAME_STACK_FAMILY: Family = allOf(TransformComp::class,
            GraphicComp::class, StackComp::class).get()
}
