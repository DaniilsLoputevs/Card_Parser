package towerdefense.ashley.engine

import com.badlogic.ashley.core.Engine
import ktx.assets.async.AssetStorage
import towerdefense.ashley.components.game.GameCardComponent.CardRank.*
import towerdefense.ashley.components.game.GameCardComponent.CardSuit.SPADES
import towerdefense.asset.CardDeckAtlas
import towerdefense.asset.GeneralAsset
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

fun Engine.initGameDefault(gameContext: GameContext, assets: AssetStorage) {
    gameContext.cards = createCardDeckDefault(assets)
    gameContext.stacks = createStacksDefault(assets)
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
