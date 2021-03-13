package towerdefense.ashley.engine

import com.badlogic.ashley.core.Engine
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
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter
import java.util.function.Predicate

fun Engine.createCard(
        textureAtlas: TextureAtlas,
        textureRegion: String,
        cardSuit: GameCardComponent.CardSuit,
        cardRank: GameCardComponent.CardRank,
        posX: Float, posY: Float
): GameCardAdapter {
    val rsl = this.entity {
        val texture: TextureAtlas.AtlasRegion = textureAtlas.findRegion(textureRegion)

        with<TransformComponent> {
            initTransformComp(texture, posX, posY)
            setSizeByHeightSAR(160f)
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
    return GameCardAdapter(rsl)
}

/**
 * temple for create GameStack
 */
fun Engine.createStack(
        textureAtlas: TextureAtlas,
        textureRegion: String,
        posX: Float, posY: Float,
        onClickFun: () -> Unit = {}
): GameStackAdapter {

    val rsl = entity {
        val texture: TextureAtlas.AtlasRegion = textureAtlas.findRegion(textureRegion)

        with<TransformComponent> {
            initTransformComp(texture, posX, posY)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
        with<GraphicComponent>() { setSpriteRegion(texture) }
        with<GameStackComponent>() {
            onClick = onClickFun
        }
    }
    return GameStackAdapter(rsl)
}