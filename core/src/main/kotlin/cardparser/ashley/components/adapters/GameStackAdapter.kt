package cardparser.ashley.components.adapters

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
class GameStackAdapter() : AbstractAdapter() {
    lateinit var transComp: TransformComponent
    lateinit var gameStackComp: GameStackComponent

    /** Short way to get cardStack from stackAdapter. */
    fun getCards(): MutableList<GameCardAdapter> = gameStackComp.cardStack

    /**
     * total hit box = stack hit box + all card hit box.(full area of stack)
     * check: contains [position] in total hit box.
     */
    fun containsPosInTotalHitBox(position: Vector2): Boolean {
        val thisPos = this.transComp.position
        val thisSize = this.transComp.size
        val offsetCorrection = CARD_STACK_OFFSET * this.gameStackComp.size()

        return thisPos.x <= position.x
                && thisPos.x + thisSize.x >= position.x // thisSize.width
                && thisPos.y - offsetCorrection <= position.y
                && thisPos.y + thisSize.y >= position.y; // thisSize.height
    }

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

    fun containsPosStack(position: Vector2): Boolean {
        return this.transComp.shape.contains(position)
    }

    /** Find area of last card and check: is [otherPos] contains. */
    private fun containsPosInLastCardHitBox(otherPos: Vector2): Boolean {
        val thisPos = this.transComp.position
        val thisSize = this.transComp.size
        val offsetCorrection = CARD_STACK_OFFSET * this.gameStackComp.size()

        return thisPos.x <= otherPos.x
                && thisPos.x + thisSize.x >= otherPos.x // thisSize.width
                && thisPos.y - offsetCorrection <= otherPos.y
                && thisPos.y - offsetCorrection + thisSize.y >= otherPos.y; // thisSize.height
    }

    fun getNextCardPosition(cardPositionBuff: Vector3) {
        if (this.gameStackComp.isEmpty()) {
            cardPositionBuff.set(
                    this.transComp.position.x,
                    this.transComp.position.y,
                    10f
            )
        } else {
            val temp = this.gameStackComp.getLast()
            cardPositionBuff.set(
                    this.transComp.position.x,
                    temp.transComp.position.y -
                            this.gameStackComp.size() * CARD_STACK_OFFSET,
                    10f + (this.gameStackComp.size() * 10f)
            )
        }
    }

    /**
     * [card] MUST CONTAINS in this stacks, else you will receive incorrect date.
     */
    fun getPosForCard(card: GameCardAdapter, cardPositionBuff: Vector3) {
        val cardIndex = this.gameStackComp.indexOf(card)
        cardPositionBuff.set(
                this.transComp.position.x,
                this.transComp.position.y - (cardIndex * CARD_STACK_OFFSET),
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
    }

    override fun toString(): String = "stack={ pos = ${transComp.position} & size = ${gameStackComp.size()} }"

}
