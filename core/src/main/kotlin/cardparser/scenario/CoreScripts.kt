package cardparser.scenario

import cardparser.CARD_HEIGHT
import cardparser.CARD_WIDTH
import cardparser.STACK_OPEN_CARD_OFFSET
import cardparser.ashley.StackCanAddLogic
import cardparser.ashley.components.*
import cardparser.ashley.components.CardComp.CardRank
import cardparser.ashley.components.CardComp.CardSuit
import cardparser.asset.GeneralAsset
import cardparser.entities.Card
import cardparser.entities.MainStack
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector3
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import java.util.*


/** Create card deck for game: 52 game cards */
fun Engine.createCardDeck(textureAtlas: TextureAtlas,
                          pos: Vector3,
                          touchable: Boolean = false,
                          open: Boolean = false
): MutableList<Card> {
    return mutableListOf<Card>().apply {
        CardSuit.values().forEach { suit ->
            val suitName = suit.name.toLowerCase(Locale.ROOT)

            CardRank.values().forEach { rank ->
                val rankName = rank.name.toLowerCase(Locale.ROOT)

                this.add(createCard(
                        textureAtlas, "${suitName}_${rankName}",
                        suit, rank, touchable, open, pos.x, pos.y
                ))
            }
        }
    }
}

fun Engine.createStackRaw(assets: AssetStorage,
                          stackCount: Int,
                          initX: Float, stableY: Float, stackGap: Float,
                          createStack: (texture: Texture, posX: Float, posY: Float, posZ: Float) -> Stack
): MutableList<Stack> {
    val list = mutableListOf<Stack>()
    var corX = initX
    for (i in 0 until stackCount) {
        corX += stackGap
        list.add(createStack(assets[GeneralAsset.CARD_STACK.desc], corX, stableY, 1f))
        corX += CARD_WIDTH
    }
    return list
}

fun Engine.createCard(textureAtlas: TextureAtlas,
                      textureRegion: String,
                      cardSuit: CardSuit,
                      cardRank: CardRank,
                      isCardTouchable: Boolean = false,
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
        with<TouchComp> { isTouchable = isCardTouchable }
    })
}

fun Engine.createBottomStack(texture: Texture, posX: Float, posY: Float, posZ: Float = 0f): Stack {
    return Stack(entity {
        with<TransformComp> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComp> { setSpriteRegion(texture) }
        with<StackComp> {
            this.stackCanAddLogic = StackCanAddLogic.KLONDAIK
            this.shiftStep = STACK_OPEN_CARD_OFFSET.toLong()
        }
        with<DragComp>()
    })
}

fun Engine.createUpStack(texture: Texture, posX: Float, posY: Float, posZ: Float = 0f): Stack {
    return Stack(entity {
        with<TransformComp> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComp> { setSpriteRegion(texture) }
        with<StackComp> {
            this.stackCanAddLogic = StackCanAddLogic.UPSTACKS
            this.shiftStep = 0L
        }
        with<DragComp>()
    })
}

fun Engine.createMainStack(
        texture: Texture,
        posX: Float, posY: Float, posZ: Float = 0f,
        _order: Int, isMain: Boolean
): MainStack {
    return MainStack(entity {
        with<TransformComp> {
            initThis(texture, posX, posY, posZ)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComp> { setSpriteRegion(texture) }
        with<MainStackComp> { this.isItMainStackOrSub = isMain }
        with<StackComp> {
            this.shiftStep = 0L
            this.stackCanAddLogic = StackCanAddLogic.UPSTACKS
        }
        if (!isMain) {
            with<DragComp>()
        }
    })
}


private val logger by lazy { loggerApp<CoreScripts>() }

class CoreScripts
