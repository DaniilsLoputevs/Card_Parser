package towerdefense.ashley.engine

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.getSystem
import ktx.assets.async.AssetStorage
import towerdefense.ashley.components.game.GameCardComponent.CardRank.*
import towerdefense.ashley.components.game.GameCardComponent.CardSuit.SPADES
import towerdefense.ashley.getOurGameCards
import towerdefense.ashley.getOurGameStacks
import towerdefense.ashley.systems.CardBindingSystem
import towerdefense.ashley.systems.DebugSystem
import towerdefense.ashley.systems.RenderSystem
import towerdefense.ashley.systems.ScreenInputSystem
import towerdefense.asset.CardBackAtlas
import towerdefense.asset.CardDeckAtlas
import towerdefense.asset.GeneralAsset
import towerdefense.gameStrucures.CardMoveProcessor
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

fun Engine.initGameDefault(assets: AssetStorage, gameContext: GameContext, gameViewport : Viewport) {

    this.run {
        createCardDeckDefault(assets)
        createStacksDefault(assets)

        getSystem<DebugSystem>().apply {
            this.gameContext = gameContext
        }
        getSystem<RenderSystem>().apply {
            this.configBackground(assets[GeneralAsset.BACKGROUND_DEFAULT.desc])
            this.configCardBack(assets[CardBackAtlas.CARD_BACK_DEFAULT.desc].findRegion("light"))
        }
        getSystem<ScreenInputSystem>().apply {
            this.inputProcessors = arrayOf(
                    CardMoveProcessor(gameContext, gameViewport, this@run.getOurGameCards())
            )
            setProcessing(true)
        }
        getSystem<CardBindingSystem>().apply {
            this.gameContext = gameContext
            this.stacks = this@run.getOurGameStacks()
            setProcessing(true)
        }
    }
}

/**
 * Create card deck for game: 62 game cards
 * * this script for: Default GameType
 */
fun Engine.createCardDeckDefault(assets: AssetStorage): List<GameCardAdapter> {
    return listOf<GameCardAdapter>(
            this.createCard(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "14_spades_ace",
                    SPADES, ACE, true, 0f, 50f),
            this.createCard(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "2_spades_two",
                    SPADES, TWO, true, 200f, 150f),
            this.createCard(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "3_spades_three",
                    SPADES, THREE, false, 400f, 150f),
            this.createCard(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "4_spades_four",
                    SPADES, FOUR, false, 600f, 150f),
            this.createCard(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "5_spades_five",
                    SPADES, FIVE, true, 800f, 150f),
    )
}

/**
 * Create game slot/stacks for card: 13 stacks
 * * this script for: Default GameType
 */
fun Engine.createStacksDefault(assets: AssetStorage): List<GameStackAdapter> {
    return listOf<GameStackAdapter>(
            createStack(assets[GeneralAsset.CARD_STACK.desc],
                    45f, 520f, ::mainStackOnClick),
            createStack(assets[GeneralAsset.CARD_STACK.desc],
                    45f, 290f),
            createStack(assets[GeneralAsset.CARD_STACK.desc],
                    250f, 290f),
    )
}

private fun mainStackOnClick() {
    println("TEST ON CLICK()")
}
