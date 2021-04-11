package towerdefense.ashley.engine

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.getSystem
import ktx.assets.async.AssetStorage
import towerdefense.CARD_WIDTH
import towerdefense.ashley.components.KlondikeGame.GameCardComponent.CardRank.*
import towerdefense.ashley.components.KlondikeGame.GameCardComponent.CardSuit.SPADES
import towerdefense.ashley.getOurGameCards
import towerdefense.ashley.getOurGameStacks
import towerdefense.ashley.systems.*
import towerdefense.asset.CardBackAtlas
import towerdefense.asset.CardDeckAtlas
import towerdefense.asset.GeneralAsset
import towerdefense.ashley.systems.parts.screeninput.CardMoveProcessor
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

fun Engine.initKlondikeGame(assets: AssetStorage, gameContext: GameContext, gameViewport: Viewport) {
    this.run {
        val cardsForInit = createCardDeckDefault(assets)
        val stacksForInit = createStacksDefault(assets)

        getSystem<DebugSystem>().apply {
            this.gameContext = gameContext
            this.stacks = this@run.getOurGameStacks()
            this.cards = this@run.getOurGameCards()
        }
        getSystem<KlondikeMainStackSystem>().apply {
            this.context = gameContext
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
                    gameContext, gameViewport,
                    this@run.getOurGameCards(), this@run.getOurGameStacks()
                )
            )
            setProcessing(true)
        }
        getSystem<CardBindingSystem>().apply {
            this.gameContext = gameContext
            this.stacks = this@run.getOurGameStacks()
            setProcessing(true)
        }
        prepareGameScriptsDefault(gameContext, cardsForInit, stacksForInit)
    }
}

private fun Engine.prepareGameScriptsDefault(
    gameContext: GameContext,
    cards: List<GameCardAdapter>,
    stacks: List<GameStackAdapter>,
) {

//    addCardsToStack(stacks[0], listOf(cards[0], cards[1], cards[2], cards[3]))
//    addCardToStack(stacks[0], cards[0])
//    unbindCardFromStack(stacks, cards[0])
}

/**
 * Create card deck for game: 62 game cards
 * * this script for: Default GameType
 */
fun Engine.createCardDeckDefault(assets: AssetStorage): List<GameCardAdapter> {
    return listOf<GameCardAdapter>(
        this.createCard(
            assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "14_spades_ace",
            SPADES, ACE, false, 0f, 50f
        ),
        this.createCard(
            assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "2_spades_two",
            SPADES, TWO, true, 200f, 150f
        ),
        this.createCard(
            assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "3_spades_three",
            SPADES, THREE, false, 400f, 150f
        ),
        this.createCard(
            assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "4_spades_four",
            SPADES, FOUR, true, 600f, 150f
        ),
        this.createCard(
            assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "5_spades_five",
            SPADES, FIVE, true, 800f, 150f
        ),
    )
}

/**
 * Create game slot/stacks for card: 13 stacks
 * * this script for: Default GameType
 */
fun Engine.createStacksDefault(assets: AssetStorage): List<GameStackAdapter> {
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
