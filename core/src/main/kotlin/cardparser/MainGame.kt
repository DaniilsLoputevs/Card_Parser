package cardparser

import cardparser.ashley.systems.*
import cardparser.asset.FontAsset
import cardparser.asset.UIAtlasAssets
import cardparser.logger.loggerApp
import cardparser.screens.LoadingScreen
import cardparser.utils.createSkin
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf

/**
 * Main Game class
 */
class MainGame : KtxGame<KtxScreen>() {

    private val logger = loggerApp<MainGame>()

    val gameViewport: Viewport by lazy {
        FitViewport(V_WORLD_WIDTH_UNITS, V_WORLD_HEIGHT_UNITS)
    }
    val stage: Stage by lazy {
        val result = Stage(gameViewport)
        Gdx.input.inputProcessor = result
        result
    }

    //    val stage: Stage by lazy { initStage() }
    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage().apply {  }
    }
    val engine: Engine by lazy { initEngine() }

    override fun dispose() {
        logger.info("start main dispose")
        assets.dispose()
        stage.dispose()
        super.dispose()
    }

    override fun create() {
//        Gdx.app.logLevel = Application.LOG_DEBUG
        logger.info("Application :: START")

        val logStartTime = System.currentTimeMillis()
        logger.info("Application - Load Initialization assets :: START")

        val uiAssets = gdxArrayOf(
            UIAtlasAssets.values().map { assets.loadAsync(it.desc) },
            FontAsset.values().map { assets.loadAsync(it.desc) }
        ).flatten()

        KtxAsync.launch {
            uiAssets.joinAll()
            createSkin(assets)
            addScreen(LoadingScreen(this@MainGame))
            setScreen<LoadingScreen>()
        }
        logger.info(
            "Application - Load Initialization assets " +
                    ":: FINISH ## time = ${(System.currentTimeMillis() - logStartTime) * 0.001f} sec"
        )
    }

    /**
     * Load required assets for: font, ui, language
     */
//    private fun prepareLoadingForInitializationAssets(): List<Deferred<Disposable>> {
//        KtxAsync.initiate()
//        /* load skin and go to LoadingScreen for remaining asset loading*/
//        return gdxArrayOf(
//                CardDeckAtlas.values().map { assets.loadAsync(it.desc) },
//                GeneralAsset.values().map { assets.loadAsync(it.desc) },
//        ).flatten()
//    }

    private fun initEngine(): Engine {
        return PooledEngine().apply {
            addSystem(DebugSystem())
            addSystem(StartGameSystem().apply { setProcessing(false) })
            addSystem(ScreenInputSystem(gameViewport).apply { setProcessing(false) })
            addSystem(MainStackSystem().apply { setProcessing(false) })
            addSystem(DragCardSystem().apply { setProcessing(false) })
            addSystem(StackBindingSystem().apply { setProcessing(false) })
            addSystem(ReturnCardsSystem().apply { setProcessing(false) })
            addSystem(CardPositionSystem().apply { setProcessing(false) })
            addSystem(TaskExecutorSystem().apply { setProcessing(false) })
            addSystem(CalculateIsTouchableSystem().apply { setProcessing(false) })
            addSystem(RenderSystem(stage, gameViewport))
        }
    }

}

