package towerdefense.ashley.components.klondikeGame

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * This component say: This Entity is Game Card.
 */
class GameCardComponent : Component, Pool.Poolable {
    var cardSuit: CardSuit = CardSuit.NONE
    var cardRank: CardRank = CardRank.NONE
    var isCardOpen: Boolean = false


    override fun reset() {
        cardSuit = CardSuit.NONE
        cardRank = CardRank.NONE
        isCardOpen = false
    }

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
