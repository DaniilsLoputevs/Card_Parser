package cardparser.ashley.objects

import cardparser.CARD_STACK_OFFSET
import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.TransformComponent
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.ashley.get

/**
 * For friendly using Entity that is a GameStack.
 * * Components never will be null.
 */
class Stack() : AbstractGameObject() {
    lateinit var transComp: TransformComponent
    lateinit var gameStackComp: GameStackComponent

    lateinit var cards: MutableList<Card>

    operator fun get(index: Int) = cards[index]

    /**
     * total hit box = stack hit box + all card hit box.(full area of stack)
     * check: contains [position] in total hit box.
     */
    fun containsPosInTotalHitBox(position: Vector2): Boolean {
        val offsetCorrection = CARD_STACK_OFFSET * this.gameStackComp.size()

        return this.pos.x <= position.x
                && this.pos.x + this.size.x >= position.x // thisSize.width
                && this.pos.y - offsetCorrection <= position.y
                && this.pos.y + this.size.y >= position.y // thisSize.height
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
        return when (this.gameStackComp.isEmpty()) {
            true -> this.transComp.shape.contains(position)
            false -> containsPosInLastCardHitBox(position)
        }
    }

    /** Find area of last card and check: is [otherPos] contains. */
    private fun containsPosInLastCardHitBox(otherPos: Vector2): Boolean {
        val offsetCorrection = CARD_STACK_OFFSET * this.gameStackComp.size()

        return this.pos.x <= otherPos.x
                && this.pos.x + this.size.x >= otherPos.x // thisSize.width
                && this.pos.y - offsetCorrection <= otherPos.y
                && this.pos.y - offsetCorrection + this.size.y >= otherPos.y // thisSize.height
    }

    @Deprecated("NOT IN USE")
    fun getNextCardPosition(cardPositionBuff: Vector3) {
        if (this.gameStackComp.isEmpty()) {
            cardPositionBuff.set(this.pos.x, this.pos.y, 10f)
        } else {
            val temp = this.gameStackComp.getLast()
            cardPositionBuff.set(
                    this.pos.x,
                    temp.pos.y - this.gameStackComp.size() * CARD_STACK_OFFSET,
                    10f + (this.gameStackComp.size() * 10f)
            )
        }
    }

    /**
     * [card] MUST CONTAINS in this stacks, else you will receive incorrect date.
     */
    @Deprecated("NOT IN USE")
    fun getPosForCard(card: Card, cardPositionBuff: Vector3) {
        val cardIndex = this.gameStackComp.indexOf(card)
        cardPositionBuff.set(
                this.pos.x,
                this.pos.y - (cardIndex * CARD_STACK_OFFSET),
                10f + (cardIndex * 10f)
        )
    }


    /* construction part */


    constructor(entity: Entity) : this() {
        this.entity = entity
    }

    override fun refreshState() {
        this.transComp = entity[TransformComponent.mapper]!!
        this.gameStackComp = entity[GameStackComponent.mapper]!!
        this.cards = gameStackComp.cardStack
    }

    override fun toString(): String = "stack={ pos = ${this.pos} & size = ${gameStackComp.size()} }"

}
