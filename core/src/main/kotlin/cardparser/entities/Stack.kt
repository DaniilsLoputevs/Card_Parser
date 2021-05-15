package cardparser.entities

import cardparser.CARD_STACK_OFFSET
import cardparser.ashley.components.StackAPI
import cardparser.ashley.components.StackComp
import cardparser.ashley.components.TransformComp
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

open class Stack() : StackAPI, GameEntity() {
    override lateinit var transComp: TransformComp
    override lateinit var stackComp: StackComp

    /**
     * total hit box = stack hit box + all card hit box.(full area of stack)
     * check: contains [position] in total hit box.
     */
    fun containsPosInTotalHitBox(position: Vector2): Boolean {
        val offsetCorrection = CARD_STACK_OFFSET * this.size()

        return this.pos().x <= position.x
                && this.pos().x + this.transfSize().x >= position.x // thisSize.width
                && this.pos().y - offsetCorrection <= position.y
                && this.pos().y + this.transfSize().y >= position.y // thisSize.height
    }

    @Deprecated("NOT IN USE")
    fun containsPosInTotalHitBox(position: Vector3): Boolean {
        val pos = Vector2(position.x, position.y)
        return containsPos(pos)
    }

    /**
     * check: contains [position] in actual hit box area.
     * actual hit box =
     * if stack empty -> stack hit box.
     * else -> last card hit box.
     */
    fun containsPos(position: Vector2): Boolean {
        return when (this.isEmpty()) {
            true -> isInShape(position)
            false -> containsPosInLastCardHitBox(position)
        }
    }

    /** Find area of last card and check: is [otherPos] contains. */
    private fun containsPosInLastCardHitBox(otherPos: Vector2): Boolean {
        val offsetCorrection = CARD_STACK_OFFSET * this.size()

        return this.pos().x <= otherPos.x
                && this.pos().x + this.transfSize().x >= otherPos.x // thisSize.width
                && this.pos().y - offsetCorrection <= otherPos.y
                && this.pos().y - offsetCorrection + this.transfSize().y >= otherPos.y // thisSize.height
    }

    @Deprecated("NOT IN USE")
    fun getNextCardPosition(cardPositionBuff: Vector3) {
        if (this.isEmpty()) {
            cardPositionBuff.set(this.pos().x, this.pos().y, 10f)
        } else {
            val temp = this.getLast()
            cardPositionBuff.set(
                    this.pos().x,
                    temp.pos().y - this.size() * CARD_STACK_OFFSET,
                    10f + (this.size() * 10f)
            )
        }
    }

    /**
     * [card] MUST CONTAINS in this stacks, else you will receive incorrect date.
     */
    @Deprecated("NOT IN USE")
    fun getPosForCard(card: Card, cardPositionBuff: Vector3) {
        val cardIndex = this.indexOf(card)
        cardPositionBuff.set(
                this.pos().x,
                this.pos().y - (cardIndex * CARD_STACK_OFFSET),
                10f + (cardIndex * 10f)
        )
    }


    /* inner part */


    constructor(entity: Entity) : this() {
        this.entity = entity
    }

    override fun toString(): String = "stack={ pos = ${this.pos()} & size = ${this.size()} }"

}
