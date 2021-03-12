package towerdefense.ashley.engine

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import ktx.assets.async.AssetStorage
import towerdefense.ashley.components.game.GameCardComponent.CardRank.ACE
import towerdefense.ashley.components.game.GameCardComponent.CardSuit.SPADES
import towerdefense.asset.CardBackAtlas
import towerdefense.asset.CardDeckAtlas
import towerdefense.gameStrucures.GameContext

fun Engine.initGameDefault(gameContext: GameContext, assets: AssetStorage) {
    gameContext.cards = createCardDeckDefault(assets)
    gameContext.stacks = createStacksDefault(assets)
}

/**
 * Create card deck for game: 62 game cards
 * * this script for: Default GameType
 */
fun Engine.createCardDeckDefault(assets: AssetStorage): Array<Entity> {
    return arrayOf(
            this.createCard(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc],
                    "ace", SPADES, ACE, 50f, 50f),
            this.createCard(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc],
                    "ace", SPADES, ACE, 100f, 100f),
            this.createCard(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc],
                    "ace", SPADES, ACE, 200f, 100f),
            this.createCard(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc],
                    "ace", SPADES, ACE, 300f, 100f),
    )
}

/**
 * Create game slot/stacks for card: 13 stacks
 * * this script for: Default GameType
 */
fun Engine.createStacksDefault(assets: AssetStorage): Array<Entity> {
    return arrayOf(
            createStack(assets[CardBackAtlas.CARD_BACK_DEFAULT.desc],
                    "light", 45f, 520f),
            createStack(assets[CardBackAtlas.CARD_BACK_DEFAULT.desc],
                    "dark", 45f, 290f),
    )
}
