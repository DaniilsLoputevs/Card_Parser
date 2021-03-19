package towerdefense.gameStrucures.adapters

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.ashley.get
import towerdefense.CARD_STACK_OFFSET
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameStackComponent
import towerdefense.ashley.toPrint

/**
 * For friendly using Entity that is a GameStack
 * * Components never will be null.
 */
data class GameStackAdapter(val entity: Entity) {
    val transComp: TransformComponent = entity[TransformComponent.mapper]!!
    val graphicComp: GraphicComponent = entity[GraphicComponent.mapper]!!
    val gameStackComp: GameStackComponent = entity[GameStackComponent.mapper]!!


    fun containsPosition(position: Vector2): Boolean {
        return when (this.gameStackComp.isEmpty()) {
            true -> this.transComp.shape.contains(position)
            false -> containsPosIfNotEmpty(position)
        }
    }

    /**
     * Find coordinates for last card and check: is it contains [otherPos].
     */
    private fun containsPosIfNotEmpty(otherPos: Vector2): Boolean {
        val thisPos = this.transComp.interpolatedPosition
        val thisSize = this.transComp.size
        val offsetCorrection = CARD_STACK_OFFSET * this.gameStackComp.size()

        return thisPos.x <= otherPos.x
                && thisPos.x + thisSize.x >= otherPos.x // thisSize.width
                && thisPos.y - offsetCorrection <= otherPos.y
                && thisPos.y - offsetCorrection + thisSize.y >= otherPos.y; // thisSize.height
    }

    fun getNextCardPosition(): Vector3 {
        println("NEXT POS")
        println("is empty = ${this.gameStackComp.isEmpty()}")
        println("NEXT POS")
        return if (this.gameStackComp.isEmpty()) {
            Vector3(
                    this.transComp.interpolatedPosition.x,
                    this.transComp.interpolatedPosition.y,
                    10f
            )
        } else {
            val temp = this.gameStackComp.getLastCard()
            Vector3(
                    this.transComp.interpolatedPosition.x,
                    temp.transComp.interpolatedPosition.y -
                            this.gameStackComp.size() * CARD_STACK_OFFSET,
                    10f + (this.gameStackComp.size() * 10f)
            )
        }
    }

    /**
     * Card MUST CONTAINS in this stacks. You nee check it before.
     */
    fun getPositionForCard(card: GameCardAdapter, cardPos: Vector3): Vector3 {
        val cardIndex = this.gameStackComp.indexOfCard(card)
        cardPos.set(
                this.transComp.interpolatedPosition.x,
                this.transComp.interpolatedPosition.y - (cardIndex * CARD_STACK_OFFSET),
                10f + (cardIndex * 10f)
        )
        return cardPos
    }

    override fun toString(): String = entity.toPrint()
}
