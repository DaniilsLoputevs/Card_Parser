package cardparser.ashley

import cardparser.ashley.components.GameCardComponent.*
import cardparser.ashley.components.GameCardComponent.CardRank.*
import cardparser.ashley.objects.Card
import cardparser.ashley.objects.Stack
import cardparser.logger.loggerApp

enum class StackLogic {
    KLONDAIK {
        override fun doLogic(stack: Stack, addedCards: List<Card>): Boolean {
            if (stack.cards.isEmpty()) return true
            val addedCard = addedCards.first()
            val stackLastCard = stack.cards.last()
            return if (stackLastCard.suit().colour != addedCard.suit().colour) {
                if (stackLastCard.rank().isIt(ACE)) {
                    false
                } else if (stackLastCard.rank().isIt(TWO) && addedCard.rank().isIt(ACE)) {
                    true
                } else {
                    stackLastCard.rank().ordinal - 1 == addedCard.rank().ordinal
                }
            } else {
                false
            }
        }
    },
    UPSTACKS {
        override fun doLogic(stack: Stack, addedCards: List<Card>): Boolean {
            if (stack.cards.isEmpty()) return true
            val addedCard = addedCards.last()
            val stackLastCard = stack.cards.last()
            return if (stack.cards.isEmpty() && addedCard.rank() == CardRank.ACE) {
                true
            } else if (stack.cards.isNotEmpty() && addedCard.suit() == stackLastCard.suit()) {
                if (stackLastCard.rank() == CardRank.ACE && addedCard.rank() == CardRank.TWO
                ) true
                else stack.cards.last().gameCardComp.cardRank.ordinal + 1 == addedCard.rank().ordinal

            } else {
                false
            }
        }
    };

    open fun doLogic(stack: Stack, addedCards: List<Card>): Boolean = true

    companion object {
        private val logger = loggerApp<StackLogic>()
    }
}
