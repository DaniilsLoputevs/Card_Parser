package cardparser.ashley

import cardparser.ashley.components.GameCardComponent
import cardparser.ashley.components.adapters.GameStackAdapter

enum class CalculateLogic {
    KLONDAIK {
        override fun doLogic(stack: GameStackAdapter) {
            val cardsInStack = stack.getCards()
            cardsInStack.asReversed().forEachIndexed { index, card ->
                if (index == 0) {
                    card.touchComp.isTouchable = true
                    card.gameCardComp.isCardOpen = true
                } else {
                    val previousCard = cardsInStack.asReversed()[index - 1]
                    card.touchComp.isTouchable =
                        previousCard.touchComp.isTouchable
                                && card.gameCardComp.cardSuit.colour != previousCard.gameCardComp.cardSuit.colour
                                && (card.gameCardComp.cardRank.ordinal == previousCard.gameCardComp.cardRank.ordinal + 1
                                || (card.gameCardComp.cardRank == GameCardComponent.CardRank.TWO
                                && previousCard.gameCardComp.cardRank == GameCardComponent.CardRank.ACE))
                }
            }
        }
    };

    open fun doLogic(stack: GameStackAdapter): Unit {}
}
