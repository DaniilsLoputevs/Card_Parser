package cardparser.ashley

import cardparser.ashley.components.CardComp.CardRank.ACE
import cardparser.ashley.components.CardComp.CardRank.TWO
import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.logger.loggerApp

enum class StackTouchLogic {
    KLONDIKE {
        override fun setTouchable(stack: Stack) {
            val reversed = stack.asReversed()
            reversed.forEachIndexed { index, card ->
                if (index == 0) {
                    card.touchable(true)
                    card.open(true)
                } else {
                    card.open(klondikeCardTouchable(reversed[index - 1], card))
                }
            }
        }

        private fun klondikeCardTouchable(prevCard: Card, card: Card): Boolean {
            return prevCard.touchable()
                    && card.open()
                    && card.suit().colour != prevCard.suit().colour
                    && ((card.rank().ordinal == prevCard.rank().ordinal + 1) || (card.rank().isIt(TWO) && prevCard.rank().isIt(ACE)))
        }
    };

    open fun setTouchable(stack: Stack) {}

    companion object {
        private val logger by lazy { loggerApp<StackTouchLogic>() }
    }
}

