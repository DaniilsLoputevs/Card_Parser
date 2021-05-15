package cardparser.ashley

import cardparser.ashley.components.CardComp.CardRank.ACE
import cardparser.ashley.components.CardComp.CardRank.TWO
import cardparser.ashley.entities.Card
import cardparser.ashley.entities.Stack
import cardparser.logger.loggerApp

enum class CalculateLogic {
    KLONDIKE {
        override fun setTouchable(stack: Stack) {
            val reversed = stack.cards().asReversed()
            reversed.forEachIndexed { index, card ->
                if (index == 0) {
                    card.touchable(true)
                    card.open(true)
                } else {
                    card.open(klondikeCardTouchable(reversed[index - 1], card))
                }
            }
        }
    };

    open fun setTouchable(stack: Stack) {}

    companion object {
        private val logger = loggerApp<CalculateLogic>()
    }
}

private fun klondikeCardTouchable(prevCard: Card, card: Card): Boolean {
    return prevCard.touchable()
            && card.open()
            && card.suit().colour != prevCard.suit().colour
            && ((card.rank().ordinal == prevCard.rank().ordinal + 1) || (card.rank().isIt(TWO) && prevCard.rank().isIt(ACE)))
}
