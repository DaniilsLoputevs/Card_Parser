package towerdefense.ashley.engine

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.ashley.entity
import ktx.ashley.with
import towerdefense.CARD_HEIGHT
import towerdefense.CARD_WIDTH
import towerdefense.ashley.components.DragAndDropComponent
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.game.GameStackComponent
import java.util.function.Predicate

fun Engine.createCard(
        textureAtlas: TextureAtlas,
        textureRegion: String,
        cardSuit: GameCardComponent.CardSuit,
        cardRank: GameCardComponent.CardRank,
        posX: Float, posY: Float
): Entity {
    return this.entity {
//        val atlas: TextureAtlas = assets[TextureAtlasAsset.TEST_CARD_BACK.descriptor]
//        val texture: TextureAtlas.AtlasRegion = atlas.findRegion("dark")


//        val atlas: TextureAtlas = assets[TextureAtlasAsset.FIRST_CARD_DECK.descriptor]
        val texture: TextureAtlas.AtlasRegion = textureAtlas.findRegion(textureRegion)
//        val texture: TextureAtlas.AtlasRegion = atlas.findRegion("two")

        with<TransformComponent> {
            initTransformComp(texture, posX, posY)
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
        with<GraphicComponent>() { setSpriteRegion(texture) }
        with<GameStackComponent>()
    }
}