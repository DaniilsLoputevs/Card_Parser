package towerdefense.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * This component say: This Entity is Game Card.
 */
class GameCardComponent : Component, Pool.Poolable {
    var cardSuit: CardSuit = CardSuit.NONE
    var cardRank: CardRank = CardRank.NONE

    var isCardOpen: Boolean = false
    var next: Entity? = null
    var isClickable: Boolean = false

    override fun reset() {

    }

    fun intCard(suit: CardSuit, rank: CardRank, cardOpen: Boolean = false) {
        cardSuit = suit
        cardRank = rank
        isCardOpen = cardOpen
    }

    companion object {
        val mapper = mapperFor<GameCardComponent>()
    }


    enum class CardRank(val power: Int) {
        JOKER(15),
        ACE(14),
        KING(13),
        QUEEN(12),
        JACK(11),
        TEN(10),
        NINE(9),
        EIGHT(8),
        SEVEN(7),
        SIX(6),
        FIVE(5),
        FOUR(4),
        THREE(3),
        TWO(2),
        NONE(-1)
    }

    enum class CardSuit(val colour: String) {
        DIAMONDS("RED"), // Бубны
        HEARTS("RED"),   // Червы/черви
        SPADES("BLACK"), // Пики
        CLUBS("BLACK"),   // Трефы
        NONE("NONE")
    }

}
