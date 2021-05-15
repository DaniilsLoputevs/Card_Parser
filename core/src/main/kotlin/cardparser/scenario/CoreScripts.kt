package cardparser.scenario

import cardparser.CARD_HEIGHT
import cardparser.CARD_STACK_OFFSET
import cardparser.CARD_WIDTH
import cardparser.ashley.StackAddPredicate
import cardparser.ashley.components.*
import cardparser.ashley.components.CardComp.CardRank
import cardparser.ashley.components.CardComp.CardSuit
import cardparser.ashley.entities.Card
import cardparser.ashley.entities.Stack
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.ashley.entity
import ktx.ashley.with
import java.util.*


/** Create card deck for game: 52 game cards */
fun Engine.createCardDeck(textureAtlas: TextureAtlas): MutableList<Card> {
    return mutableListOf<Card>().apply {
        CardSuit.values().forEach { suit ->
            val suitName = suit.name.toLowerCase(Locale.ROOT)

            CardRank.values().forEach { rank ->
                val rankName = rank.name.toLowerCase(Locale.ROOT)

                this.add(
                        createCard(
                                textureAtlas, "${suitName}_${rankName}",
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
    return Card(this.entity {
        val texture: TextureAtlas.AtlasRegion = textureAtlas.findRegion(textureRegion)
        with<TransformComp> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
//            setSizeByHeightSAR(160f)
        }
        with<GraphicComp> { setSpriteRegion(texture) }
        with<CardComp> { init(cardSuit, cardRank, isCardOpen) }
        with<TouchComp> { isTouchable = true }
    })
}

/**
 * temple for create GameStack
 */
fun Engine.createStack(
        texture: Texture,
        posX: Float, posY: Float, posZ: Float = 0f
): Stack {
    val rsl = entity {
        with<TransformComp> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComp> { setSpriteRegion(texture) }
        with<StackComp> {
            this.stackAddPredicate = StackAddPredicate.KLONDAIK
            this.shiftRange = CARD_STACK_OFFSET.toLong()
        }
        with<DragComp>()
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
        with<TransformComp> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComp> { setSpriteRegion(texture) }
        with<StackComp> {
            this.stackAddPredicate = StackAddPredicate.UPSTACKS
            this.shiftRange = 0L
        }
        with<DragComp>()
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
        with<TransformComp> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComp> { setSpriteRegion(texture) }
        with<MainStackComp> { this.order = _order }
        with<StackComp> {
            this.shiftRange = 0L
            this.stackAddPredicate = StackAddPredicate.UPSTACKS
        }
        if (_order == 1) {
            with<DragComp>()
        }
    }
    return Stack(rsl)
}
