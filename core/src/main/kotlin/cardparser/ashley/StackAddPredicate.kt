package cardparser.ashley

import cardparser.ashley.components.CardComp.CardRank.ACE
import cardparser.ashley.components.CardComp.CardRank.TWO
import cardparser.ashley.components.StackAPI
import cardparser.ashley.entities.Card
import cardparser.logger.loggerApp

enum class StackAddPredicate {
    KLONDAIK {
        override fun check(stack: StackAPI, addedCards: List<Card>): Boolean {
            if (stack.cards().isEmpty()) return true
            val addedCard = addedCards.first()
            val stackLastCard = stack.cards().last()
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
        override fun check(stack: StackAPI, addedCards: List<Card>): Boolean {
//            logger.dev("UPSTACKS :: check - invoked")
//            logger.dev("predicate", stack.cards().isEmpty())
//            if (stack.cards().isEmpty()) return true
            val addedCard = addedCards.last()
//            val stackLastCard = stack.cards().last()
//            logger.dev("card new ", addedCard)
//            logger.dev("card last", addedCard)
            return if (stack.cards().isEmpty() && addedCard.rank().isIt(ACE)) {
                true
            } else if (stack.cards().isNotEmpty()) {
                val stackLastCard = stack.cards().last()

                if (addedCard.suit() == stackLastCard.suit()) {
                    if (stackLastCard.rank().isIt(ACE) && addedCard.rank().isIt(TWO)
                    ) true
                    else stack.cards().last().rank().ordinal + 1 == addedCard.rank().ordinal
                } else false

            } else {
                false
            }
        }
    };

    open fun check(stack: StackAPI, addedCards: List<Card>): Boolean = true

    companion object {
        private val logger = loggerApp<StackAddPredicate>()
    }
}
