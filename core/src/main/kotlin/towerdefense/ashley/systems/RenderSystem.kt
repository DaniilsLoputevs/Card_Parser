package towerdefense.ashley.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.error
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2
import towerdefense.V_WORLD_HEIGHT_UNITS
import towerdefense.V_WORLD_WIDTH_UNITS
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.findComponent
import towerdefense.event.GameEvent
import towerdefense.event.GameEventListener
import towerdefense.event.GameEventManager

private val LOG = logger<RenderSystem>()
private const val BGD_SCROLL_SPEED_X = 0.03f
private const val MIN_BGD_SCROLL_SPEED_Y = -0.25f
private const val OUTLINE_COLOR_RED = 0f
private const val OUTLINE_COLOR_GREEN = 113f / 255f
private const val OUTLINE_COLOR_BLUE = 214f / 255f
private const val OUTLINE_COLOR_MIN_ALPHA = 0.35f
private const val BGD_SCROLL_SPEED_GAIN_BOOST_1 = 0.25f
private const val BGD_SCROLL_SPEED_GAIN_BOOST_2 = 0.5f
private const val TIME_TO_RESET_BGD_SCROLL_SPEED = 10f // in seconds

/**
 * Component processor && Game Event Listener.
 * Rendering all [Entity] with components:
 * [GraphicComponent]
 * [TransformComponent]
 */
class RenderSystem(
        private val stage: Stage,
        private var outlineShader: ShaderProgram,
        private val gameViewport: Viewport,
        private val gameEventManager: GameEventManager,
        backgroundTexture: Texture
) : SortedIteratingSystem(
        allOf(GraphicComponent::class, TransformComponent::class).get(),
        compareBy { entity -> entity[TransformComponent.mapper] }
), GameEventListener {
    private val batch: Batch = stage.batch

    private val background = Sprite(backgroundTexture).apply {
//        println("DEV background")
//        println("background h " + this.height)
//        println("background w " + this.width)
//        println("DEV BEFORE")
//
        // pixels but equals to WU, so World is 1280 x 720, - background as well
        setSize(V_WORLD_WIDTH_UNITS.toFloat(),   V_WORLD_HEIGHT_UNITS.toFloat())
//
//        println("DEV AFTER")
//        println("background h " + this.height)
//        println("background w " + this.width)
//        println("DEV background")
    }


//    private val backgroundScrollSpeed = vec2(BGD_SCROLL_SPEED_X, MIN_BGD_SCROLL_SPEED_Y)
//    private val textureSizeLoc = outlineShader.getUniformLocation("u_textureSize")
//    private val outlineColorLoc = outlineShader.getUniformLocation("u_outlineColor")
//    private val outlineColor = Color(OUTLINE_COLOR_RED, OUTLINE_COLOR_GREEN, OUTLINE_COLOR_BLUE, 1f)
//    private val playerEntities by lazy {
//        engine.getEntitiesFor(
//            allOf(PlayerComponent::class).exclude(RemoveComponent::class).get()
//        )
//    }

    /**
     * Called when this EntitySystem is added to an {@link Engine}.
     * @param engine The {@link Engine} this system was added to.
     */
    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
//        println("DEV")

//        println("background boundingRectangle " + background.boundingRectangle)
//        println("background height " + background.height)
//        println("background width " + background.width)
//        println("background" + background)
//        println("batch " + gameViewport.screenX)
//        println("batch " + gameViewport.screenY)
//        println("batch " + gameViewport.worldWidth)
//        println("batch " + gameViewport.worldHeight)
//        println("batch " + gameViewport.)


//        println("stage height ${stage.height}")
//        println("stage width ${stage.width}")
//        println("stage camera position ${stage.camera.position}")
//        println("gameViewport screenWidth ${gameViewport.screenWidth}")
//        println("gameViewport screenX ${gameViewport.screenX}")
//        println("gameViewport screenY ${gameViewport.screenY}")
//        println("gameViewport worldHeight ${gameViewport.worldHeight}")
//        println("gameViewport worldWidth ${gameViewport.worldWidth}")
//        println("gameViewport scaling ${gameViewport.scaling}")
//        println("gameViewport camera combined \n${gameViewport.camera.combined}")
//        println("gameViewport camera position \n${gameViewport.camera.position}")
//        println("gameViewport camera view \r\n${gameViewport.camera.view}")
//        println("DEV")
//        gameEventManager.addListener(GameEvent.PowerUp::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
//        gameEventManager.removeListener(GameEvent.PowerUp::class, this)
    }

    override fun update(deltaTime: Float) {
        // render background
        renderBackground(deltaTime)

        // render entities
        renderEntity(deltaTime)

//        // render player with outline shader in case he has a shield
//        renderEntityOutlines()
    }

    private fun renderEntityOutlines() {
//        batch.use(camera.combined) {
//            it.shader = outlineShader
//            playerEntities.forEach { entity ->
//                renderPlayerOutlines(entity, it)
//            }
//            it.shader = null
//        }
    }

    private fun renderBackground(deltaTime: Float) {
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
        val transformComp = entity.findComponent(TransformComponent.mapper)
        val graphicComp = entity.findComponent(GraphicComponent.mapper)

        val (posX, posY) = transformComp.interpolatedPosition
        val (sizeX, sizeY) = transformComp.size
        if (graphicComp.sprite.texture == null) {
            LOG.error { "Entity has no texture for rendering" }
            return
        }

        graphicComp.sprite.run {
            rotation = transformComp.rotationDeg
            // setBounds(...) == setPosition(...) && setSize(...)
            setBounds(posX, posY, sizeX, sizeY)
            draw(batch)
        }
    }

    override fun onEvent(event: GameEvent) {
//        val eventPowerUp = event as GameEvent.PowerUp
//        if (eventPowerUp.type == PowerUpType.SPEED_1) {
//            backgroundScrollSpeed.y -= BGD_SCROLL_SPEED_GAIN_BOOST_1
//        } else if (eventPowerUp.type == PowerUpType.SPEED_2) {
//            backgroundScrollSpeed.y -= BGD_SCROLL_SPEED_GAIN_BOOST_2
//        }
    }
}
