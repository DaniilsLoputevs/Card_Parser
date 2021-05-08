package cardparser.scenario

import cardparser.CARD_HEIGHT
import cardparser.CARD_STACK_OFFSET
import cardparser.CARD_WIDTH
import cardparser.ashley.Logic
import cardparser.ashley.components.*
import cardparser.ashley.components.GameCardComponent.CardRank
import cardparser.ashley.components.GameCardComponent.CardSuit
import cardparser.ashley.objects.Card
import cardparser.ashley.objects.Stack
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.ashley.entity
import ktx.ashley.with
import java.util.*


/** Create card deck for game: 52 game cards */
fun Engine.createCardDeck(textureAtlas: TextureAtlas): MutableList<Card> {
    return mutableListOf<Card>().apply {
        var textureIndex = 0
        CardSuit.values().forEach { suit ->
            val suitName = suit.name.toLowerCase(Locale.ROOT)

            CardRank.values().forEach { rank ->
                val rankName = rank.name.toLowerCase(Locale.ROOT)

                this.add(
                        createCard(
                                textureAtlas, "${textureIndex++}_${suitName}_${rankName}",
                                suit, rank, false, 0f, 0f
                        )
                )
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
): Card {
    val rsl = this.entity {
        val texture: TextureAtlas.AtlasRegion = textureAtlas.findRegion(textureRegion)
        with<TransformComponent> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
//            setSizeByHeightSAR(160f)
        }
        with<GraphicComponent> {
            setSpriteRegion(texture)
        }
        with<GameCardComponent> {
            this.cardSuit = cardSuit
            this.cardRank = cardRank
            this.isCardOpen = isCardOpen
        }
        with<TouchComponent> {
            isTouchable = true
        }
    }
    return Card(rsl)
}

/**
 * temple for create GameStack
 */
fun Engine.createStack(
        texture: Texture,
        posX: Float, posY: Float, posZ: Float = 0f
): Stack {
    val rsl = entity {
        with<TransformComponent> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComponent> { setSpriteRegion(texture) }
        with<GameStackComponent> {
            this.logic = Logic.KLONDAIK
            this.shiftRange = CARD_STACK_OFFSET.toLong()
        }
        with<StandardDragComponent>()
    }
    return Stack(rsl)
}

/**
 * temple for create FoundationGameStack
 */
fun Engine.createFoundationStack(
        texture: Texture,
        posX: Float, posY: Float, posZ: Float = 0f
): Stack {
    val rsl = entity {
        with<TransformComponent> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComponent> { setSpriteRegion(texture) }
        with<GameStackComponent> {
            this.shiftRange = 0L
            this.logic = Logic.UPSTACKS
        }
        with<StandardDragComponent>()
    }
    return Stack(rsl)
}

/**
 * temple for create GameStack
 */
fun Engine.createMainStack(
        texture: Texture,
        posX: Float, posY: Float, posZ: Float = 0f,
        _order: Int
): Stack {
    val rsl = entity {
        with<TransformComponent> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComponent> { setSpriteRegion(texture) }
        with<MainStackComponent> { this.order = _order }
        with<GameStackComponent> {
            this.shiftRange = 0L
            this.logic = Logic.UPSTACKS
        }
        if (_order == 1) {
            with<StandardDragComponent>()
        }
    }
    return Stack(rsl)
}
