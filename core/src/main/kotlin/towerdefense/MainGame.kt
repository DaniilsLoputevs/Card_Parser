package towerdefense

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.info
import ktx.log.logger
import towerdefense.ashley.systems.CardBindingSystem
import towerdefense.ashley.systems.DebugSystem
import towerdefense.ashley.systems.RenderSystem
import towerdefense.ashley.systems.ScreenInputSystem
import towerdefense.asset.CardDeckAtlas
import towerdefense.asset.GeneralAsset
import towerdefense.event.GameEventManager
import towerdefense.screens.LoadingScreen

/**
 * Main Game class
 */
class MainGame : KtxGame<KtxScreen>() {

    private val logger = logger<MainGame>()
    val gameViewport: Viewport by lazy {
        FitViewport(V_WORLD_WIDTH_UNITS, V_WORLD_HEIGHT_UNITS)
    }
    val stage: Stage by lazy {
        val result = Stage(gameViewport)
        Gdx.input.inputProcessor = result
        result
    }

    //    val stage: Stage by lazy { initStage() }
    val assets: AssetStorage by lazy { initAssetStorage() }
    val gameEventManager by lazy { GameEventManager() }
    val engine: Engine by lazy { initEngine() }


    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        logger.info { "Application :: START" }
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
//        println("GAME :: create() ## END")
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
                CardDeckAtlas.values().map { assets.loadAsync(it.desc) },
                GeneralAsset.values().map { assets.loadAsync(it.desc) },
        ).flatten()
    }

    private fun initEngine(): Engine {
        return PooledEngine().apply {
            addSystem(DebugSystem())
            addSystem(
                    ScreenInputSystem().apply {
                        setProcessing(false)
                    }
            )
            addSystem(
                    CardBindingSystem().apply {
                        setProcessing(false)
                    }
            )
            addSystem(
                    RenderSystem(
                            stage,
//                            ShaderProgramStub(),
//                    assets[ShaderProgramAsset.OUTLINE.descriptor],
                            gameViewport,
//                            gameEventManager,
//                            Sprite(assets[TextureAtlasAsset.MY_FIRST_ATLAS.descriptor].createSprites()[0])
//                    assets[TextureAtlasAsset.BACKGROUND.descriptor].createSprite("main-screen-background")
//                            assets[ImgAsset.BACKGROUND_DEFAULT.desc]
                    )
            )
//            addSystem(RemoveSystem(gameEventManager))
//        }

        }
    }

}

