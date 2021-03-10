package towerdefense.ashley

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import towerdefense.CARD_HEIGHT
import towerdefense.CARD_WIDTH
import towerdefense.ashley.components.DragAndDropComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.game.GameCardComponent.CardSuit.*
import towerdefense.ashley.components.game.GameCardComponent.CardRank.*
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameStacksComponent
import towerdefense.asset.TextureAtlasAsset
import java.util.function.Predicate

fun Engine.createTestCardDeck(
        assets: AssetStorage
): Array<Entity> {
    return arrayOf(
            createTestGameCard(assets[TextureAtlasAsset.FIRST_CARD_DECK.descriptor],
                    "ace", SPADES, ACE),
            createTestGameCard(assets[TextureAtlasAsset.FIRST_CARD_DECK.descriptor],
                    "ace", SPADES, ACE)
    )
}

fun Engine.createTestGameCard(
        textureAtlas: TextureAtlas,
        textureRegion: String,
        cardSuit: GameCardComponent.CardSuit,
        cardRank: GameCardComponent.CardRank
): Entity {
    return entity {
//        val atlas: TextureAtlas = assets[TextureAtlasAsset.TEST_CARD_BACK.descriptor]
//        val texture: TextureAtlas.AtlasRegion = atlas.findRegion("dark")


//        val atlas: TextureAtlas = assets[TextureAtlasAsset.FIRST_CARD_DECK.descriptor]
        val texture: TextureAtlas.AtlasRegion = textureAtlas.findRegion(textureRegion)
//        val texture: TextureAtlas.AtlasRegion = atlas.findRegion("two")

        with<TransformComponent> {
            initTransformComp(texture)
            setSizeByHeightSAR(160f)
//            this.setSizeByWidthSAR(114f)
        }
        with<GraphicComponent>() {
            setSpriteRegion(texture)
        }
        with<GameCardComponent>() {
            isClickable = true
            this.cardSuit = cardSuit
            this.cardRank = cardRank
//            setNextPredicate = Predicate{other -> this.cardRank < other.cardRank
//                    && this.cardSuit.colour != other.cardSuit.colour}
            setNextPredicate = Predicate { true }
        }
        with<DragAndDropComponent>() {}
    }
}

/**
 * Create 13 stacks for default gameType
 */
fun Engine.createStacks(
        assets: AssetStorage
): Array<Entity> {
    return arrayOf(
            createStack(assets[TextureAtlasAsset.TEST_CARD_BACK.descriptor],
                    "light", 45f, 520f),
            createStack(assets[TextureAtlasAsset.TEST_CARD_BACK.descriptor],
                    "dark", 45f, 290f),
    )
}

/**
 * temple for create GameStack
 */
fun Engine.createStack(
        textureAtlas: TextureAtlas,
        textureRegion: String,
        posX: Float, posY: Float
): Entity {

    return entity {
        val texture: TextureAtlas.AtlasRegion = textureAtlas.findRegion(textureRegion)

        with<TransformComponent> {
            initTransformComp(texture, posX, posY)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComponent>() {
            setSpriteRegion(texture)
        }
        with<GameStacksComponent>()
    }
}
