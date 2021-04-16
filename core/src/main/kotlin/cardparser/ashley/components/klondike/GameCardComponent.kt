package cardparser.ashley.components.klondike

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * This component say: This Entity is Game Card.
 */
class GameCardComponent : Component, Pool.Poolable {
    lateinit var cardSuit: CardSuit
    lateinit var cardRank: CardRank
    var isCardOpen: Boolean = false


    override fun reset() {
        isCardOpen = false
    }

    override fun toString(): String = "GameCardComponent"

    companion object {
        val mapper = mapperFor<GameCardComponent>()
    }

    enum class CardRank {
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
        ACE
    }

    enum class CardSuit(val colour: String) {
        SPADES("BLACK"),  // Пики
        HEARTS("RED"),    // Червы/черви
        CLUBS("BLACK"),   // Трефы
        DIAMONDS("RED")   // Бубны
    }

}
