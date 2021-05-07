package cardparser.ashley

import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter

enum class StartGameLogic {
    KLONDIKE_START {
        var startLogic = true
        var processLogic = true

        override fun doLogic(
            cards: MutableList<GameCardAdapter>,
            startStack: GameStackAdapter,
            stacksList: List<GameStackAdapter>
        ): Boolean {
            var z = 0f
            return when {
                startLogic -> {
                    cards.forEach {
                        it.transComp.position.set(startStack.transComp.position.x, startStack.transComp.position.y, z++)
                        it.gameCardComp.isCardOpen = false
                        it.touchComp.isTouchable = false
                    }
                    startLogic = false
                    true
                }
                processLogic -> {
                    stacksList.forEachIndexed { index, stack ->
                        if (stack.getCards().size < (index + 1)) {
                            cards.removeLastOrNull()?.let { stack.getCards().add(it) }
                            if (stack.getCards().size == (index + 1)) {
                                stack.getCards().last().run {
                                    gameCardComp.isCardOpen = true
                                    touchComp.isTouchable = true
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
        cards: MutableList<GameCardAdapter>,
        startStack: GameStackAdapter,
        stacksList: List<GameStackAdapter>
    ): Boolean = true

}
