package towerdefense.gameStrucures.adapters

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.ashley.get
import towerdefense.CARD_STACK_OFFSET
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.KlondikeGame.GameStackComponent
import towerdefense.ashley.toPrint

/**
 * For friendly using Entity that is a GameStack
 * * Components never will be null.
 */
data class GameStackAdapter(val entity: Entity) {
    val transComp: TransformComponent = entity[TransformComponent.mapper]!!
    val gameStackComp: GameStackComponent = entity[GameStackComponent.mapper]!!

    /**
     * short way to get cardstack fom stackadapter
     */
    fun getCards(): MutableList<GameCardAdapter> = gameStackComp.cardStack

    /**
     * total hit box = stack hit box + all card hit box.(full area of stack)
     * check: contains [position] in total hit box.
     */
    fun containsPosInTotalHitBox(position: Vector2) :Boolean {
        val thisPos = this.transComp.position
        val thisSize = this.transComp.size
        val offsetCorrection = CARD_STACK_OFFSET * this.gameStackComp.size()

        return thisPos.x <= position.x
                && thisPos.x + thisSize.x >= position.x // thisSize.width
                && thisPos.y - offsetCorrection <= position.y
                && thisPos.y + thisSize.y >= position.y; // thisSize.height
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

    /**
     * Find area of last card and check: is [otherPos] contains.
     */
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
    fun getPosForCard(card: GameCardAdapter, cardPositionBuff: Vector3){
        val cardIndex = this.gameStackComp.indexOf(card)
        cardPositionBuff.set(
                this.transComp.position.x,
                this.transComp.position.y - (cardIndex * CARD_STACK_OFFSET),
                10f + (cardIndex * 10f)
        )
    }

    override fun toString(): String = entity.toPrint()
}
