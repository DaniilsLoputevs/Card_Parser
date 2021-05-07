package cardparser.ashley

import cardparser.ashley.components.GameCardComponent
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter

enum class Logic {
    KLONDAIK {
        override fun doLogic(stack: GameStackAdapter, addedCards: List<GameCardAdapter>): Boolean {
            val cardComp = addedCards.first().gameCardComp;
            if (stack.getCards().isEmpty()) {
                return true
            }
            val stackComp = stack.getCards().last().gameCardComp
            return if (stackComp.cardSuit.colour != cardComp.cardSuit.colour) {
                if (stackComp.cardRank == GameCardComponent.CardRank.ACE) {
                    false
                } else if (stackComp.cardRank == GameCardComponent.CardRank.TWO && cardComp.cardRank == GameCardComponent.CardRank.ACE) {
                    true
                } else stackComp.cardRank.ordinal - 1 == cardComp.cardRank.ordinal
            } else {
                false
            }
        }
    },
    UPSTACKS {
        override fun doLogic(stack: GameStackAdapter, addedCards: List<GameCardAdapter>): Boolean {
            val cardComp = addedCards.last().gameCardComp;
            return if (stack.getCards().isEmpty() && cardComp.cardRank == GameCardComponent.CardRank.ACE)  {
                true
            } else if (stack.getCards().size > 0 && cardComp.cardSuit == stack.getCards().last().gameCardComp.cardSuit) {
                println(stack.getCards().last().gameCardComp)
                if (stack.getCards().last().gameCardComp.cardRank == GameCardComponent.CardRank.ACE &&
                    cardComp.cardRank == GameCardComponent.CardRank.TWO
                ) {
                    true
                } else {
                    stack.getCards().last().gameCardComp.cardRank.ordinal + 1 == cardComp.cardRank.ordinal
                }
            } else {
                false
            }
        }
    };

    open fun doLogic(stack: GameStackAdapter, addedCards: List<GameCardAdapter>): Boolean = true
}
