package cardparser.scenario

import cardparser.CARD_WIDTH
import cardparser.STACK_GAP_STEP
import cardparser.ashley.CalculateLogic
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.ashley.systems.*
import cardparser.asset.CardBackAtlas
import cardparser.asset.CardDeckAtlas
import cardparser.asset.GeneralAsset
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.getSystem
import ktx.assets.async.AssetStorage
import kotlin.random.Random

fun Engine.initKlondaikGame(assets: AssetStorage, gameViewport: Viewport) {
    this.run {
        val cards = createCardDeck(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc])
        createStacksKlondaik(assets, cards.toMutableList())
//        getSystem<StartGameSystem>().apply {
//
//        }
        getSystem<ScreenInputSystem>().apply {
            this.gameViewport = gameViewport
            setProcessing(true)
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
fun Engine.createStacksKlondaik(assets: AssetStorage, cards: MutableList<GameCardAdapter>): List<GameStackAdapter> {
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
    randomCards(cards, list)
    list.forEach { it.getCards().last().gameCardComp.isCardOpen = true }
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
            STACK_GAP_STEP,
            520f,
            _order = 0
        )
    )
    list.add(
        createMainStack(
            assets[GeneralAsset.CARD_STACK.desc],
            STACK_GAP_STEP * 2 + CARD_WIDTH,
            520f,
            _order = 1
        )
    )
    list[11].gameStackComp.cardStack = cards
    return list;
}

fun randomCards(cards: MutableList<GameCardAdapter>, stacks: List<GameStackAdapter>): Unit {
    var random: Int
    stacks.forEachIndexed { index, stack ->
        for (i in 0..index) {
            random = (0..1000).random()
            stack.getCards().add(cards.removeAt(random and (cards.size - 1)))
        }
    }
}

