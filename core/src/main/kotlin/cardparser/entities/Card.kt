package cardparser.entities

import cardparser.ashley.components.*
import com.badlogic.ashley.core.Entity

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
}
