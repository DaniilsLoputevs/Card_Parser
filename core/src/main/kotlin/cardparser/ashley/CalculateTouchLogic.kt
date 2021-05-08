package cardparser.ashley

import cardparser.ashley.components.GameCardComponent.CardRank.ACE
import cardparser.ashley.components.GameCardComponent.CardRank.TWO
import cardparser.ashley.objects.Card
import cardparser.ashley.objects.Stack
import cardparser.logger.loggerApp

enum class CalculateLogic {
    KLONDAIK {
        override fun doLogic(stack: Stack) {
            stack.cards.asReversed().forEachIndexed { index, card ->
                if (index == 0) {
                    card.touchable(true)
                    card.open(true)
                } else {
                    val prevCard = stack.cards.asReversed()[index - 1]
                    card.open(klondikeCardTouchable(prevCard, card))
                }
            }
        }
    };

    open fun doLogic(stack: Stack): Unit {}

    companion object {
        private val logger = loggerApp<CalculateLogic>()
    }
}

private fun klondikeCardTouchable(prevCard: Card, card: Card): Boolean {
    return prevCard.isTouchable()
            && card.isOpen()
            && card.suit().colour != prevCard.suit().colour
            && ((card.rank().ordinal == prevCard.rank().ordinal + 1) || (card.rank().isIt(TWO) && prevCard.rank().isIt(ACE)))
}
