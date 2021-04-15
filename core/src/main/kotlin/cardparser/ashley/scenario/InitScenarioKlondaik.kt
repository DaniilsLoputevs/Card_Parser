package cardparser.ashley.scenario

import cardparser.CARD_WIDTH
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.ashley.components.klondike.GameCardComponent.CardRank.*
import cardparser.ashley.systems.*
import cardparser.ashley.systems.parts.screeninput.CardMoveProcessor
import cardparser.asset.CardBackAtlas
import cardparser.asset.CardDeckAtlas
import cardparser.asset.GeneralAsset
import cardparser.event.GameEvent
import cardparser.event.GameEventManager
import cardparser.event.listeners.CardBindingListener
import cardparser.gameStrucures.GameContext
import cardparser.gameStrucures.GameRepository
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.getSystem
import ktx.assets.async.AssetStorage

fun Engine.initKlondaikGame(assets: AssetStorage, gameViewport: Viewport) {
    this.run {
        val gameRep = GameRepository(
                createCardDeck(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc]),
                createStacksKlondaik(assets)
        )
        val eventManager = GameEventManager().apply {
            this.addListener(GameEvent.BindingCards::class, CardBindingListener(gameRep))
        }

        getSystem<DebugSystem>().apply {
            this.gameRep = gameRep
        }
        getSystem<MainStackSystem>().apply {
            this.gameViewport = gameViewport
            this.context = GameContext
            this.gameRep = gameRep
            setProcessing(true)
        }
        getSystem<RenderSystem>().apply {
            this.configBackground(assets[GeneralAsset.BACKGROUND_DEFAULT.desc])
            this.configCardBack(assets[CardBackAtlas.CARD_BACK_DEFAULT.desc].findRegion("light"))
        }
        getSystem<ScreenInputSystem>().apply {
            this.inputProcessors = arrayOf(
                    CardMoveProcessor(gameViewport, gameRep, eventManager)
            )
            setProcessing(true)
        }
//        prepareGameScriptsKlondaik(gameRep)
    }
}

private fun Engine.prepareGameScriptsKlondaik(
        gemRep: GameRepository
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
