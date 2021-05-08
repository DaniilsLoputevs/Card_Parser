package cardparser.ashley.systems

import cardparser.CARD_HEIGHT
import cardparser.CARD_WIDTH
import cardparser.V_WORLD_HEIGHT_UNITS
import cardparser.V_WORLD_WIDTH_UNITS
import cardparser.ashley.components.GameCardComponent
import cardparser.ashley.components.GraphicComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.findComp
import cardparser.logger.loggerApp
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
) {
    private val logger = logger<RenderSystem>()
    private val batch: Batch = stage.batch

    var background: Sprite = Sprite()
    var cardBackSprite: Sprite = Sprite()


    fun configBackground(backgroundTexture: Texture) {
        background.apply {
            setRegion(backgroundTexture)
            // pixels but equals to WU, so World is 1280 x 720, - background as well
            setSize(V_WORLD_WIDTH_UNITS, V_WORLD_HEIGHT_UNITS)
        }
    }

    fun configCardBack(cardBackTexture: TextureRegion) {
        cardBackSprite.apply {
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
        val transformComp = entity.findComp(TransformComponent.mapper)
        val graphicComp = entity.findComp(GraphicComponent.mapper)

        if (graphicComp.sprite.texture == null) {
            logger.error { "Entity has no texture for rendering" }
            return
        }

        /* If card Closed we need to ignore card's texture and render card back texture,
         after that finish method */
        val currentSprite = when {
            entity.contains(GameCardComponent.mapper)
                    && isCardClose(entity) -> cardBackSprite
            else -> graphicComp.sprite
        }

        val (posX, posY) = transformComp.position
        val (sizeX, sizeY) = transformComp.size

        currentSprite.run {
            // setBounds(...) == setPosition(...) && setSize(...)
            setBounds(posX, posY, sizeX, sizeY)
            draw(batch)
        }
    }


    /** just pretty API. */
    private fun isCardClose(entity: Entity): Boolean = !entity[GameCardComponent.mapper]!!.isCardOpen

    companion object {
        private val logger = loggerApp<RenderSystem>()
    }
}
