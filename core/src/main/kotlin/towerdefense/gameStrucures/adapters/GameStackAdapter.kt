package towerdefense.gameStrucures.adapters

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
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

    /**
     * If Stack is empty, we return:
     * this.transComp
     * If Stack is NOT empty, we return:
     * gameStackComp.getLastCard().tansComp
     *
     * This method need to correct working Binding System.
     */
//    fun getActualHitBox(): TransformComponent {
//        return if (this.gameStackComp.isEmpty()) this.transComp
//        else this.gameStackComp.getLastCard().transComp
//    }

    fun containsPosition(position: Vector2): Boolean {
        return when (this.gameStackComp.isEmpty()) {
            true -> this.transComp.shape.contains(position)
            false -> contains(position)
        }
    }

    fun contains(otherPos: Vector2): Boolean {
        val thisPos = this.transComp.interpolatedPosition
        val thisSize = this.transComp.size
        val offsetCorrection = when (this.gameStackComp.isEmpty()) {
            true -> 0f
            false -> CARD_STACK_OFFSET * this.gameStackComp.size()
        }
        return thisPos.x <= otherPos.x
                && thisPos.x + thisSize.x >= otherPos.x // thisSize.width
                && thisPos.y - offsetCorrection <= otherPos.y
                && thisPos.y - offsetCorrection + thisSize.y >= otherPos.y; // thisSize.height
    }

    @Deprecated("should use getPosForIndex(cardIndexInStack : Int), but latter, not now")
    fun getNextCardPosition(): Vector2 {
        println("NEXT POS")
        println("is empty = ${this.gameStackComp.isEmpty()}")
        println("NEXT POS")
        return if (this.gameStackComp.isEmpty()) {
            Vector2(
                    this.transComp.interpolatedPosition.x,
                    this.transComp.interpolatedPosition.y
            )
        } else {
            val temp = this.gameStackComp.getLastCard()
            Vector2(
                    this.transComp.interpolatedPosition.x,
                    temp.transComp.interpolatedPosition.y -
                            this.gameStackComp.size() * CARD_STACK_OFFSET
            )
        }
    }

    /**
     * Card MUST CONTAINS in this stacks. You nee check it before.
     */
    fun getPosForCard(card: GameCardAdapter, cardPos: Vector2) :Vector2 {
        val cardIndex = this.gameStackComp.indexOfCard(card)
        cardPos.set(
                this.transComp.interpolatedPosition.x,
                this.transComp.interpolatedPosition.y - (cardIndex * CARD_STACK_OFFSET)
                )
        return cardPos
    }

    override fun toString(): String {
        return entity.toPrint()
    }
}
