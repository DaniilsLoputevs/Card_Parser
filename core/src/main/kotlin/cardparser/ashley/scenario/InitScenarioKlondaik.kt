package cardparser.ashley.scenario

import cardparser.CARD_WIDTH
import cardparser.ashley.components.klondike.GameCardComponent.CardRank.*
import cardparser.ashley.getOurGameCards
import cardparser.ashley.getOurGameStacks
import cardparser.ashley.systems.*
import cardparser.ashley.systems.parts.screeninput.CardMoveProcessor
import cardparser.asset.CardBackAtlas
import cardparser.asset.CardDeckAtlas
import cardparser.asset.GeneralAsset
import cardparser.gameStrucures.GameContext
import cardparser.gameStrucures.adapters.GameCardAdapter
import cardparser.gameStrucures.adapters.GameStackAdapter
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.getSystem
import ktx.assets.async.AssetStorage

fun Engine.initKlondaikGame(assets: AssetStorage, gameViewport: Viewport) {
    this.run {
        val cardsForInit = createCardDeck(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc])
        val stacksForInit = createStacksKlondaik(assets)

        getSystem<DebugSystem>().apply {
            this.stacks = this@run.getOurGameStacks()
            this.cards = this@run.getOurGameCards()
        }
        getSystem<MainStackSystem>().apply {
            this.context = GameContext
            this.gameViewport = gameViewport
            setProcessing(true)
        }
        getSystem<RenderSystem>().apply {
            this.configBackground(assets[GeneralAsset.BACKGROUND_DEFAULT.desc])
            this.configCardBack(assets[CardBackAtlas.CARD_BACK_DEFAULT.desc].findRegion("light"))
        }
        getSystem<ScreenInputSystem>().apply {
            this.inputProcessors = arrayOf(
                    CardMoveProcessor(
                            gameViewport,
                            this@run.getOurGameCards(), this@run.getOurGameStacks()
                    )
            )
            setProcessing(true)
        }
        getSystem<CardBindingSystem>().apply {
            this.stacks = this@run.getOurGameStacks()
            setProcessing(true)
        }
        prepareGameScriptsKlondaik(cardsForInit, stacksForInit)
    }
}

private fun Engine.prepareGameScriptsKlondaik(
        cards: List<GameCardAdapter>,
        stacks: List<GameStackAdapter>,
) {

//    addCardsToStack(stacks[0], listOf(cards[0], cards[1], cards[2], cards[3]))
//    addCardToStack(stacks[0], cards[0])
//    unbindCardFromStack(stacks, cards[0])
}

/**
 * Create game slot/stacks for card: 13 stacks
 * * this script for: Default GameType
 */
fun Engine.createStacksKlondaik(assets: AssetStorage): List<GameStackAdapter> {
    val list = mutableListOf<GameStackAdapter>()
    var corX = 0f
    for (i in 0..6) {
        corX += 60.25f
        list.add(
                createStack(
                        assets[GeneralAsset.CARD_STACK.desc],
                        corX,
                        290f,
                        onClickFun = ::mainStackOnClick
                )
        )
        if (i > 2) {
            list.add(
                    createFoundationStack(
                            assets[GeneralAsset.CARD_STACK.desc],
                            corX,
                            520f,
                            onClickFun = ::mainStackOnClick
                    )
            )
        }
        corX += CARD_WIDTH
    }
    list.add(
            createMainStack(
                    assets[GeneralAsset.CARD_STACK.desc],
                    60.25f,
                    520f,
                    _order = 0,
                    onClickFun = ::mainStackOnClick
            )
    )
    list.add(
            createMainStack(
                    assets[GeneralAsset.CARD_STACK.desc],
                    60.25f * 2 + CARD_WIDTH,
                    520f,
                    _order = 1,
                    onClickFun = ::mainStackOnClick
            )
    )
    return list;
}

private fun mainStackOnClick() {
    println("TEST ON CLICK()")
}


/**
return listOf<GameStackAdapter>(
createStack(assets[GeneralAsset.CARD_STACK.desc],
45f, 520f, onClickFun = ::mainStackOnClick),
createStack(assets[GeneralAsset.CARD_STACK.desc],
45f, 290f),
createStack(assets[GeneralAsset.CARD_STACK.desc],
250f, 290f),
)
 */
