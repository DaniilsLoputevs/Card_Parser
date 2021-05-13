package cardparser.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

interface CardAPI {
    var cardComp: CardComp

    fun suit(): CardComp.CardSuit = cardComp.cardSuit
    fun rank(): CardComp.CardRank = cardComp.cardRank
    fun open(): Boolean = cardComp.isCardOpen
    fun open(value: Boolean) = run { cardComp.isCardOpen = value }

}

/**
 * This component say: This Entity is Game Card.
 */
class CardComp : Component, Pool.Poolable {
    lateinit var cardSuit: CardSuit
    lateinit var cardRank: CardRank
    var isCardOpen: Boolean = false

    fun init(cardSuit: CardSuit, cardRank: CardRank, isCardOpen: Boolean) {
        this.cardSuit = cardSuit; this.cardRank = cardRank; this.isCardOpen = isCardOpen
    }


    override fun reset() {
        isCardOpen = false
    }

    override fun toString(): String = "GameCardComponent"

    companion object {
        val mapper = mapperFor<CardComp>()
    }

    enum class CardRank {
        ACE,
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
        KING;

        fun isIt(value: CardRank): Boolean = this == value
    }

    enum class CardSuit(val colour: String) {
        SPADES("BLACK"),  // Пики
        HEARTS("RED"),    // Червы/черви
        CLUBS("BLACK"),   // Трефы
        DIAMONDS("RED")   // Бубны
    }

}
