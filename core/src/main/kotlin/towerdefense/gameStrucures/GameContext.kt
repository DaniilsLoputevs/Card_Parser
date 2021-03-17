package towerdefense.gameStrucures

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import towerdefense.CARD_HEIGHT
import towerdefense.CARD_WIDTH
import towerdefense.V_WORLD_HEIGHT_UNITS
import towerdefense.V_WORLD_WIDTH_UNITS
import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.NONE
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

class GameContext {
    var background: Sprite = Sprite()
    var cardBack: Sprite = Sprite()

    lateinit var stacks: List<GameStackAdapter>
    lateinit var cards: List<GameCardAdapter>

    var touchingCard: GameCardAdapter? = null
    var touchingCardStatus: CardMoveProcessor.DragAndDropStatus = NONE



    fun configByPlayerChose(backgroundTexture: Texture, cardBackTexture: TextureRegion) {
        background.apply {
            setRegion(backgroundTexture)
            // pixels but equals to WU, so World is 1280 x 720, - background as well
            setSize(V_WORLD_WIDTH_UNITS.toFloat(), V_WORLD_HEIGHT_UNITS.toFloat())
        }
        cardBack.apply {
            setRegion(cardBackTexture)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
    }


}