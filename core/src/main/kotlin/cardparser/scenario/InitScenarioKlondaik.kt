package cardparser.scenario

import cardparser.CARD_WIDTH
import cardparser.STACK_GAP_STEP
import cardparser.ashley.CalculateLogic
import cardparser.ashley.StartGameLogic
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.ashley.systems.*
import cardparser.asset.CardBackAtlas
import cardparser.asset.CardDeckAtlas
import cardparser.asset.GeneralAsset
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.getSystem
import ktx.assets.async.AssetStorage

fun Engine.initKlondaikGame(assets: AssetStorage, gameViewport: Viewport) {
    this.run {
        var cardsSet = createCardDeck(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc])
        cardsSet = randomCards(cardsSet.toMutableList())
        val mainStack = createStartStackKlondike(assets)
        val bottomStacks = createBottomStacksKlondike(assets)
        val upStacks = createUpStacksKlondike(assets)
        cardsSet.forEach { it.transComp.position.set(mainStack.transComp.position) }
        getSystem<StartGameSystem>().apply {
            cards = cardsSet
            stack = mainStack
            stackList = bottomStacks
            logic = StartGameLogic.KLONDIKE_START
            touchSystem = getSystem<ScreenInputSystem>()
            setProcessing(true)
        }
        getSystem<ScreenInputSystem>().apply {
            this.gameViewport = gameViewport
            setProcessing(false)
        }
        getSystem<MainStackSystem>().apply {
            setProcessing(true)
        }
        getSystem<StandardStackBindingSystem>().apply {
            setProcessing(true)
        }
        getSystem<CardPositionSystem>().apply {
            setProcessing(true)
        }
        getSystem<StandardDragCardSystem>().apply {
            setProcessing(true)
        }
        getSystem<ReturnCardsSystem>().apply {
            setProcessing(true)
        }
        getSystem<CalculateIsTouchableSystem>().apply {
            logic = CalculateLogic.KLONDAIK
            setProcessing(true)
        }
        getSystem<RenderSystem>().apply {
            this.configBackground(assets[GeneralAsset.BACKGROUND_DEFAULT.desc])
            this.configCardBack(assets[CardBackAtlas.CARD_BACK_DEFAULT.desc].findRegion("light"))
        }
//        prepareGameScriptsKlondaik(gameRep)
    }
}

//    addCardsToStack(stacks[0], listOf(cards[0], cards[1], cards[2], cards[3]))
//    addCardToStack(stacks[0], cards[0])
//    unbindCardFromStack(stacks, cards[0])

/**
 * Create game slot/stacks for card: 13 stacks
 * * this script for: Default GameType
 */
fun Engine.createBottomStacksKlondike(assets: AssetStorage): List<GameStackAdapter> {
    val list = mutableListOf<GameStackAdapter>()
    var corX = 0f
    for (i in 0..6) {
        corX += STACK_GAP_STEP
        list.add(
            createStack(
                assets[GeneralAsset.CARD_STACK.desc],
                corX,
                290f
            )
        )
        corX += CARD_WIDTH
    }
    return list;
}

fun Engine.createUpStacksKlondike(assets: AssetStorage): List<GameStackAdapter> {
    val list = mutableListOf<GameStackAdapter>()
    var corX = 0f
    corX = STACK_GAP_STEP * 3 + CARD_WIDTH * 3
    for (i in 0..3) {
        corX += STACK_GAP_STEP
        list.add(
            createFoundationStack(
                assets[GeneralAsset.CARD_STACK.desc],
                corX,
                520f
            )
        )
        corX += CARD_WIDTH
    }
    list.add(
        createMainStack(
            assets[GeneralAsset.CARD_STACK.desc],
            STACK_GAP_STEP * 2 + CARD_WIDTH,
            520f,
            _order = 1
        )
    )
    return list;
}

fun Engine.createStartStackKlondike(assets: AssetStorage): GameStackAdapter {
    return createMainStack(
        assets[GeneralAsset.CARD_STACK.desc],
        STACK_GAP_STEP,
        520f,
        _order = 0
    )
}

fun randomCards(cards: MutableList<GameCardAdapter>): MutableList<GameCardAdapter> {
    val random = MathUtils.random
    val list = mutableListOf<GameCardAdapter>()
    while (cards.size != 0) {
        list.add(cards.removeAt(random.nextInt(1000) and (cards.size - 1)))
    }
    return list
}

