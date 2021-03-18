package towerdefense.ashley.engine

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.ashley.entity
import ktx.ashley.with
import towerdefense.CARD_HEIGHT
import towerdefense.CARD_WIDTH
import towerdefense.ashley.components.TouchComponent
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
        isCardOpen: Boolean = false,
        posX: Float = 0f, posY: Float = 0f, posZ : Float = 1f
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
            this.setNextPredicate = Predicate { true }
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
        posX: Float, posY: Float, posZ : Float = 0f,
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