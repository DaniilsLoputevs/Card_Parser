package towerdefense.ashley.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.contains
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.error
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2
import towerdefense.CARD_HEIGHT
import towerdefense.CARD_WIDTH
import towerdefense.V_WORLD_HEIGHT_UNITS
import towerdefense.V_WORLD_WIDTH_UNITS
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.event.GameEvent
import towerdefense.event.GameEventListener
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.GameCardAdapter

/**
 * Component processor && Game Event Listener.
 * Rendering all [Entity] with components:
 * [GraphicComponent]
 * [TransformComponent]
 */
class RenderSystem(
        private val stage: Stage,
        private val gameViewport: Viewport,
) : SortedIteratingSystem(
        allOf(GraphicComponent::class, TransformComponent::class).get(),
        compareBy { entity -> entity[TransformComponent.mapper] }
){
    private val logger = logger<RenderSystem>()
    private val batch: Batch = stage.batch

    var background: Sprite = Sprite()
    var cardBack: Sprite = Sprite()



    fun configBackground(backgroundTexture: Texture) {
        background.apply {
            setRegion(backgroundTexture)
            // pixels but equals to WU, so World is 1280 x 720, - background as well
            setSize(V_WORLD_WIDTH_UNITS, V_WORLD_HEIGHT_UNITS)
        }
    }
    fun configCardBack(cardBackTexture: TextureRegion) {
        cardBack.apply {
            setRegion(cardBackTexture)
            setSize(CARD_WIDTH, CARD_HEIGHT)
        }
    }

    /**
     * Called when this EntitySystem is added to an {@link Engine}.
     * @param engine The {@link Engine} this system was added to.
     */
    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
    }

    override fun update(deltaTime: Float) {

        // render background
        renderBackground()

        // render entities
        renderEntity(deltaTime)
    }

    private fun renderBackground() {
        if (background.texture == null) return
        stage.viewport.apply()
        batch.use(stage.camera.combined) {
            background.draw(it)
        }
    }

    private fun renderEntity(deltaTime: Float) {
        // * set for "It must sort then Method sort() will be invoked", cause default it must not be.
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
            // * in {super.update(deltaTime)} - invoke Method sort()
            // super.update(deltaTime) - call processEntity forEach in sortedEntities
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformComp = entity.findRequiredComponent(TransformComponent.mapper)
        val graphicComp = entity.findRequiredComponent(GraphicComponent.mapper)

        if (graphicComp.sprite.texture == null) {
            logger.error { "Entity has no texture for rendering" }
            return
        }

        /* If card Closed we need to ignore card's texture and render card back texture,
         after that finish method */
        if (entity.contains(GameCardComponent.mapper)) {
            val card = GameCardAdapter(entity)
            if (!card.gameCardComp.isCardOpen) {
                renderCardBack(card)
                return
            }

        }

        val (posX, posY) = transformComp.interpolatedPosition
        val (sizeX, sizeY) = transformComp.size

        graphicComp.sprite.run {
            rotation = transformComp.rotationDeg
            // setBounds(...) == setPosition(...) && setSize(...)
            setBounds(posX, posY, sizeX, sizeY)
            draw(batch)
        }
    }

    private fun renderCardBack(card: GameCardAdapter) {
        val (posX, posY) = card.transComp.interpolatedPosition
        val (sizeX, sizeY) = card.transComp.size

        cardBack.run {
            rotation = card.transComp.rotationDeg
            // setBounds(...) == setPosition(...) && setSize(...)
            setBounds(posX, posY, sizeX, sizeY)
            draw(batch)
        }
    }

}
