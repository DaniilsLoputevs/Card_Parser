package towerdefense

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.joinAll
import towerdefense.ashley.systems.RenderSystem
import towerdefense.event.GameEventManager
import towerdefense.asset.TextureAtlasAsset
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.info
import ktx.log.logger
import towerdefense.asset.TextureAsset
import towerdefense.stubs.ShaderProgramStub
import towerdefense.screens.LoadingScreen

/**
 * Main Game class
 */
class MainGame : KtxGame<KtxScreen>() {

    private val logger = logger<MainGame>()
//    val gameViewport = FitViewport(2f, 2f)
    val gameViewport = FitViewport(64f, 32f)
//    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val stage: Stage by lazy { initStage() }
    val assets: AssetStorage by lazy { initAssetStorage() }
    val gameEventManager by lazy { GameEventManager() }
    val engine: Engine by lazy { initEngine() }


    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        println("DEV")
        println("gameViewport screenHeight ${gameViewport.screenHeight}")
        println("gameViewport screenWidth ${gameViewport.screenWidth}")
        println("gameViewport screenX ${gameViewport.screenX}")
        println("gameViewport screenY ${gameViewport.screenY}")
        println("gameViewport worldHeight ${gameViewport.worldHeight}")
        println("gameViewport worldWidth ${gameViewport.worldWidth}")
        println("gameViewport scaling ${gameViewport.scaling}")
        println("gameViewport camera combined \n${gameViewport.camera.combined}")
        println("gameViewport camera position \n${gameViewport.camera.position}")
        println("gameViewport camera view \r\n${gameViewport.camera.view}")
        println("DEV")

        logger.debug { "MainGame :: create() ## START" }
        logger.info { "MainGame :: Load Initialization assets - START" }
        val logStartTime = System.currentTimeMillis();


        val asyncJobsForLoading = prepareLoadingForInitializationAssets()
        KtxAsync.launch {
            asyncJobsForLoading.joinAll()

            /* go to LoadingScreen to load remaining assets */
            addScreen(LoadingScreen(this@MainGame))
            setScreen<LoadingScreen>()
        }

        logger.info { "MainGame :: Load Initialization assets - FINISH time: ${(System.currentTimeMillis() - logStartTime) * 0.001f} sec" }
        println("GAME :: create() ## END")
    }

    /**
     * this method will ALWAYS invoke with this.currentScreen#render()
     * Log Example:
     * MainGame :: render invoke time
     * GameScreen :: render invoke time
     * MainGame :: render invoke time
     * GameScreen :: render invoke time
     */
    override fun render() {
        super.render()
    }

    /** Init part */

    private fun initStage(): Stage {
        val result = Stage(FitViewport(V_UI_WIDTH_PIXELS.toFloat(), V_UI_HEIGHT_PIXELS.toFloat()))
        Gdx.input.inputProcessor = result
        return result
    }

    private fun initAssetStorage(): AssetStorage {
        KtxAsync.initiate()
        return AssetStorage()
    }

    /**
     * Load required assets for: font, ui, language
     */
    private fun prepareLoadingForInitializationAssets(): List<Deferred<Disposable>> {
        KtxAsync.initiate()

        /* load skin and go to LoadingScreen for remaining asset loading*/
        return gdxArrayOf(
                TextureAtlasAsset.values().map { assets.loadAsync(it.descriptor) },
                TextureAsset.values().map { assets.loadAsync(it.descriptor) },
        ).flatten()
    }

    private fun initEngine(): Engine {
        return PooledEngine().apply {
//            val atlas = assets[TextureAtlasAsset.GRAPHICS.descriptor]

//            addSystem(DebugSystem(gameEventManager, audioService))
//            addSystem(MoveSystem(gameEventManager).apply {
//                setProcessing(false)
//            })
//            addSystem(AttachSystem())
            addSystem(
                    RenderSystem(
                            stage,
                            ShaderProgramStub(),
//                    assets[ShaderProgramAsset.OUTLINE.descriptor],
                            gameViewport,
                            gameEventManager,
//                            Sprite(assets[TextureAtlasAsset.MY_FIRST_ATLAS.descriptor].createSprites()[0])
//                    assets[TextureAtlasAsset.BACKGROUND.descriptor].createSprite("main-screen-background")
                            assets[TextureAsset.DEFAULT_BACKGROUND.descriptor]
                    )
            )
//            addSystem(RemoveSystem(gameEventManager))
//        }

        }
    }

}

