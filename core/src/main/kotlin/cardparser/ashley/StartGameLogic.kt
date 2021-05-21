package cardparser.ashley

import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.logger.loggerApp

@Deprecated("NOT IN USE")
enum class StartGameLogic {
    KLONDIKE_START {
        var startLogic = true
        var processLogic = true

        override fun start(
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
                        if (stack.size() < (index + 1)) {
                            cards.removeLastOrNull()?.let { stack.add(it) }
                            if (stack.size() == (index + 1)) {
                                stack.last().run {
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

    open fun start(
            cards: MutableList<Card>,
            startStack: Stack,
            stacksList: List<Stack>
    ): Boolean = true

    companion object {
        private val logger = loggerApp<StartGameLogic>()
    }
}
