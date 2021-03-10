package towerdefense.ashley

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import towerdefense.ashley.components.DragAndDropComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.asset.TextureAtlasAsset

fun Engine.createTestCardDeck(
        assets: AssetStorage
): Array<Entity> {
    return arrayOf(createTestCardBack(assets));
}

fun Engine.createTestCardBack(
        assets: AssetStorage
): Entity {
    /*ship*/

    val testCardBack: Entity = this.entity {
//        val atlas: TextureAtlas = assets[TextureAtlasAsset.TEST_CARD_BACK.descriptor]
//        val texture: TextureAtlas.AtlasRegion = atlas.findRegion("dark")


        val atlas: TextureAtlas = assets[TextureAtlasAsset.FIRST_CARD_DECK.descriptor]
        val texture: TextureAtlas.AtlasRegion = atlas.findRegion("ace")
//        val texture: TextureAtlas.AtlasRegion = atlas.findRegion("two")

        with<TransformComponent> {
            initTransformComp(texture)
            this.setSizeByHeightSAR(160f)
//            this.setSizeByWidthSAR(114f)
        }
        with<GraphicComponent>() {
            setSpriteRegion(texture)
        }
        with<GameCardComponent>() {
            isClickable = true
        }
        with<DragAndDropComponent>() {}
    }

    return testCardBack;
}


/**
 * positionX & positionY - count in WU.
 */
fun Engine.createStack(
        assets: AssetStorage,
        positionX: Float,
        positionY: Float
): Entity {
    /*ship*/

    val testCardBack: Entity = entity {
        val atlas: TextureAtlas = assets[TextureAtlasAsset.TEST_CARD_BACK.descriptor]
        val texture: TextureAtlas.AtlasRegion = atlas.findRegion("light")

        with<TransformComponent> {
            initTransformComp(texture)
            this.setSizeByHeightSAR(160f)
//            this.setSizeByWidthSAR(114f)
        }
        with<GraphicComponent>() {
            setSpriteRegion(texture)
        }
    }

    return testCardBack;
}

