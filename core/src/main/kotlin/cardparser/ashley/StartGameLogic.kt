package cardparser.ashley

import cardparser.ashley.entities.Card
import cardparser.ashley.entities.Stack
import cardparser.logger.loggerApp

enum class StartGameLogic {
    KLONDIKE_START {
        var startLogic = true
        var processLogic = true

        override fun doLogic(
                cards: MutableList<Card>,
                startStack: Stack,
                stacksList: List<Stack>
        ): Boolean {
            var z = 0f
            return when {
                startLogic -> {
                    cards.forEach {
                        it.setPos(startStack.pos().x, startStack.pos().y, z++)
                        it.touchable(false)
                        it.open(false)
                    }
                    startLogic = false
                    true
                }
                processLogic -> {
                    stacksList.forEachIndexed { index, stack ->
                        if (stack.cards().size < (index + 1)) {
                            cards.removeLastOrNull()?.let { stack.cards().add(it) }
                            if (stack.cards().size == (index + 1)) {
                                stack.cards().last().run {
                                    this.touchable(true)
                                    this.open(true)
                                }
                            }
                            return true
                        }
                    }
                    processLogic = false
                    false
                }
                else -> {
                    false
                }
            }
        }
    };

    open fun doLogic(
            cards: MutableList<Card>,
            startStack: Stack,
            stacksList: List<Stack>
    ): Boolean = true

    companion object {
        private val logger = loggerApp<StartGameLogic>()
    }
}
