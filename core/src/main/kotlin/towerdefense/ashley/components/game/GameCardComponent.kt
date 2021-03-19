package towerdefense.ashley.components.game

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import towerdefense.CARD_STACK_OFFSET
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.gameStrucures.adapters.GameCardAdapter
import java.util.function.Predicate

/**
 * This component say: This Entity is Game Card.
 */
class GameCardComponent : Component, Pool.Poolable {
    var cardSuit: CardSuit = CardSuit.NONE
    var cardRank: CardRank = CardRank.NONE

    var isCardOpen: Boolean = false
    var next: GameCardAdapter? = null
    lateinit var setNextPredicate: Predicate<GameCardComponent>


    /**
     * TODO - TEST IT!!!
     */
    fun moveNextCards(newPosition: Vector2) {
        moveNextCard(newPosition.apply { y += CARD_STACK_OFFSET })
//        recursiveMoveNextCard(this, newPosition.apply { y += CARD_STACK_OFFSET })
    }

    private fun moveNextCard(newPosition: Vector2) {
//        if (this.next == null) return
//        var curr = this.next
//        while (curr != null) {
//            println("While endless")
//            curr.transComp.setTotalPosition(newPosition)
//            curr = curr.gameCardComp.next
//        }
    }

//    private fun recursiveMoveNextCard(gameCardComp: GameCardComponent, newPosition: Vector2) {
//        println("REC START")
//        println("this  =                    ${this.hashCode()}")
//        println("gameCardComp.next =        ${gameCardComp.next.hashCode()}")
//        if (gameCardComp.next == null) return
//        val transComp = gameCardComp.next!!.findRequiredComponent(TransformComponent.mapper)
//        transComp.setTotalPosition(newPosition)
//        val nextGameCardComp = gameCardComp.next!!.findRequiredComponent(GameCardComponent.mapper)
//
//        if (nextGameCardComp.next == null) return
//
//        println("nextGameCardComp =         ${nextGameCardComp.hashCode()}")
//        println("nextGameCardComp.next =    ${nextGameCardComp.next.hashCode()}")
//        recursiveMoveNextCard(nextGameCardComp, newPosition.apply { y += CARD_STACK_OFFSET })
//        println("REC END")
//    }


    override fun reset() {
        cardSuit = CardSuit.NONE
        cardRank = CardRank.NONE

        isCardOpen = false
        next = null
    }


//    fun intCard(suit: CardSuit, rank: CardRank, cardOpen: Boolean = false) {
//        cardSuit = suit
//        cardRank = rank
//        isCardOpen = cardOpen
//    }

    companion object {
        val mapper = mapperFor<GameCardComponent>()
    }


    enum class CardRank {
        NONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE,
        JOKER
    }

    enum class CardSuit(val colour: String) {
        DIAMONDS("RED"), // Бубны
        HEARTS("RED"),   // Червы/черви
        SPADES("BLACK"), // Пики
        CLUBS("BLACK"),   // Трефы
        NONE("NONE")
    }

}
