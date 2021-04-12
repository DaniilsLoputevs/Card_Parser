package towerdefense.ashley.scenario

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector3
import ktx.ashley.entity
import ktx.ashley.with
import towerdefense.CARD_HEIGHT
import towerdefense.CARD_WIDTH
import towerdefense.ashley.components.FoundationStackComponent
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TouchComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.klondike.GameCardComponent
import towerdefense.ashley.components.klondike.GameCardComponent.CardRank
import towerdefense.ashley.components.klondike.GameCardComponent.CardSuit
import towerdefense.ashley.components.klondike.GameStackComponent
import towerdefense.ashley.components.klondike.MainStackComponent
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter
import java.util.*


/** Create card deck for game: 52 game cards */
fun Engine.createCardDeck(textureAtlas: TextureAtlas): List<GameCardAdapter> {
    return mutableListOf<GameCardAdapter>().apply {
        var textureIndex = 0
        CardSuit.values().forEach { suit ->
            val suitName = suit.name.toLowerCase(Locale.ROOT)

            CardRank.values().forEach { rank ->
                val rankName = rank.name.toLowerCase(Locale.ROOT)

                this.add(createCard(
                        textureAtlas, "${textureIndex++}_${suitName}_${rankName}",
                        suit, rank, true, 0f, 0f
                ))
            }
        }
    }
}

fun Engine.createCard(
        textureAtlas: TextureAtlas,
        textureRegion: String,
        cardSuit: CardSuit,
        cardRank: CardRank,
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
 * temple for create FoundationGameStack
 */
fun Engine.createFoundationStack(
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
        with<FoundationStackComponent>()
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
        with<MainStackComponent>() { this.order = _order }
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
