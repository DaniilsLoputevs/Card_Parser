package towerdefense.ashley.scenario

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector3
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import towerdefense.CARD_HEIGHT
import towerdefense.CARD_WIDTH
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TouchComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.KlondikeGame.GameCardComponent
import towerdefense.ashley.components.KlondikeGame.GameStackComponent
import towerdefense.ashley.components.KlondikeGame.KlondikeMainStackComponent
import towerdefense.asset.CardDeckAtlas
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

/**
 * Create card deck for game: 62 game cards
 * * this script for: Default GameType
 */
fun Engine.createCardDeck(assets: AssetStorage): List<GameCardAdapter> {
    return listOf<GameCardAdapter>(
            this.createCard(
                    assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "14_spades_ace",
                    GameCardComponent.CardSuit.SPADES, GameCardComponent.CardRank.ACE, false, 0f, 50f
            ),
            this.createCard(
                    assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "2_spades_two",
                    GameCardComponent.CardSuit.SPADES, GameCardComponent.CardRank.TWO, true, 200f, 150f
            ),
            this.createCard(
                    assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "3_spades_three",
                    GameCardComponent.CardSuit.SPADES, GameCardComponent.CardRank.THREE, false, 400f, 150f
            ),
            this.createCard(
                    assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "4_spades_four",
                    GameCardComponent.CardSuit.SPADES, GameCardComponent.CardRank.FOUR, true, 600f, 150f
            ),
            this.createCard(
                    assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], "5_spades_five",
                    GameCardComponent.CardSuit.SPADES, GameCardComponent.CardRank.FIVE, true, 800f, 150f
            ),
    )
}

fun Engine.createCard(
        textureAtlas: TextureAtlas,
        textureRegion: String,
        cardSuit: GameCardComponent.CardSuit,
        cardRank: GameCardComponent.CardRank,
        isCardOpen: Boolean = false,
        posX: Float = 0f, posY: Float = 0f, posZ: Float = 1f
): GameCardAdapter {
    val rsl = this.entity {
        val texture: TextureAtlas.AtlasRegion = textureAtlas.findRegion(textureRegion)

        with<TransformComponent> {
            initTransformComp(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
//            setSizeByHeightSAR(160f)
        }
        with<GraphicComponent>() { setSpriteRegion(texture) }
        with<GameCardComponent>() {
            this.cardSuit = cardSuit
            this.cardRank = cardRank
//            setNextPredicate = Predicate{other -> this.cardRank < other.cardRank
//                    && this.cardSuit.colour != other.cardSuit.colour}
            this.isCardOpen = isCardOpen
        }
        with<TouchComponent>() {}
    }
    return GameCardAdapter(rsl)
}

/**
 * temple for create GameStack
 */
fun Engine.createStack(
        texture: Texture,
        posX: Float, posY: Float, posZ: Float = 0f,
        onClickFun: () -> Unit = {}
): GameStackAdapter {

    val rsl = entity {

        with<TransformComponent> {
            initTransformComp(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComponent>() { setSpriteRegion(texture) }
        with<GameStackComponent>() { onClick = onClickFun }
    }
    return GameStackAdapter(rsl)
}

/**
 * temple for create GameStack
 */
fun Engine.createMainStack(
        texture: Texture,
        posX: Float,
        posY: Float,
        posZ: Float = 0f,
        _order: Int,
        onClickFun: () -> Unit = {}
): GameStackAdapter {

    val rsl = entity {
        with<TransformComponent> {
            initTransformComp(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComponent>() { setSpriteRegion(texture) }
        with<KlondikeMainStackComponent>() { this.order = _order }
        with<GameStackComponent>() { onClick = onClickFun }
    }
    return GameStackAdapter(rsl)
}


/* OLD API  ---  maybe for delete */


@Deprecated("unusebale")
fun addCardsToStack(stack: GameStackAdapter, cards: List<GameCardAdapter>) {
    cards.forEach { addCardToStack(stack, it) }
}

@Deprecated("unusebale")
fun addCardToStack(stack: GameStackAdapter, card: GameCardAdapter) {
    val cardPos = Vector3()
    /* Getting position for new card before add it into stack */
    stack.getNextCardPosition(cardPos)

    stack.gameStackComp.add(card)
    card.transComp.setPosition(cardPos)

}

@Deprecated("unusebale")
fun unbindCardFromStack(stacks: List<GameStackAdapter>, card: GameCardAdapter) {
    stacks.find { it.gameStackComp.contains(card) }
            ?.let { it.gameStackComp.remove(card) }
}
