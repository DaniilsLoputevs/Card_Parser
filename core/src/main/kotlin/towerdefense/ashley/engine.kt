package towerdefense.ashley

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import towerdefense.ashley.components.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import towerdefense.UNIT_SCALE
import towerdefense.V_WORLD_HEIGHT_UNITS
import towerdefense.V_WORLD_WIDTH_UNITS
import towerdefense.ashley.components.GraphicComponent
import towerdefense.asset.TextureAtlasAsset

private const val SHIP_FIRE_OFFSET_X = 1f // in pixels
private const val SHIP_FIRE_OFFSET_Y = -6f // in pixels
const val PLAYER_START_SPEED = 3f


fun Engine.createTestCardBack(
        assets: AssetStorage,
        spawnX: Float = V_WORLD_WIDTH_UNITS * 0.5f,
        spawnY: Float = V_WORLD_HEIGHT_UNITS * 0.5f
): Entity {
    /*ship*/

    val testCardBack: Entity = entity {

        val atlas : TextureAtlas = assets[TextureAtlasAsset.TEST_CARD_BACK.descriptor]
        val texture : TextureAtlas.AtlasRegion = atlas.findRegion("dark")


        with<TransformComponent> {

            println("DEV")
            println("texture.originalWidth ${texture.originalWidth}")
            println("texture.originalHeight ${texture.originalHeight}")
            println("texture.originalHeight * UNITS  ${ texture.originalWidth * UNIT_SCALE}")
            println("texture.originalHeight * UNITS ${ texture.originalHeight * UNIT_SCALE}")
            println("DEV END")
            this.size.set(
//                    50f, 70f
//                    texture.originalWidth * UNIT_SCALE,
//                    texture.originalHeight * UNIT_SCALE
                    texture.originalWidth.toFloat(),
                    texture.originalHeight.toFloat()
            )

//            val x = spawnX - size.x * 0.5f
//            val y = spawnY - size.y * 0.5f
            val x = 0f
            val y = 0f
            println("DEV")
            println("position x $x")
            println("position y $y")
            println("DEV")
            this.setInitialPosition(
                    x, y,
//                    spawnX - size.x * 0.5f,
//                    spawnY - size.y * 0.5f,
//                    -5f,0f,
                    1f
            )
        }
        with<GraphicComponent>() {

//            val atlas : TextureAtlas = assets[TextureAtlasAsset.TEST_CARD_BACK.descriptor]
//            val playerGraphicRegion = atlas.findRegion("dark")
//            println("DEV")
//            println("atlas ${atlas.regions}")
//            println("playerGraphicRegion ${playerGraphicRegion}")
//            println("DEV")

            setSpriteRegion(texture)
        }
    }

    return testCardBack;
}


/* Simon part */


//fun Engine.createPlayer(
//    assets: AssetStorage,
////    spawnX: Float = V_WIDTH * 0.5f,
////    spawnY: Float = V_HEIGHT * 0.5f
//): Entity {
//    /*ship*/
//
//    val ship: Entity = entity {
//        with<PlayerComponent>()
//        with<FacingComponent>()
//        with<MoveComponent> {
//            speed.y = PLAYER_START_SPEED
//        }
//        with<TransformComponent> {
//            val atlas = assets[TextureAtlasAsset.GRAPHICS.descriptor]
//            val playerGraphicRegion = atlas.findRegion("ship_base")
//            size.set(
//                playerGraphicRegion.originalWidth * UNIT_SCALE,
//                playerGraphicRegion.originalHeight * UNIT_SCALE
//            )
//            setInitialPosition(
//                spawnX - size.x * 0.5f,
//                spawnY - size.y * 0.5f,
//                1f
//            )
//        }
//        with<GraphicComponent>()
//    }
//
///* fire effect of ship */
//    entity {
//        with<TransformComponent>()
//        with<AttachComponent> {
//            entity = ship
//            offset.set(
//                SHIP_FIRE_OFFSET_X * UNIT_SCALE,
//                SHIP_FIRE_OFFSET_Y * UNIT_SCALE
//            )
//        }
//        with<GraphicComponent>()
//        with<AnimationComponent> {
//            type = FIRE
//        }
//    }
//    return ship
//}

//fun Engine.createDarkMatter() {
//    entity {
//        with<TransformComponent> {
//            size.set(
//                V_WIDTH.toFloat(),
//                DAMAGE_AREA_HEIGHT
//            )
//        }
//        with<AnimationComponent> {
//            type = DARK_MATTER
//        }
//        with<GraphicComponent>()
//    }
//}

