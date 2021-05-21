package cardparser

import cardparser.ashley.systems.*
import cardparser.logger.loggerApp
import cardparser.screens.LoadingScreen
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync

/**
 * Main Game class
 */
class MainGame : KtxGame<KtxScreen>() {
    val gameViewport: Viewport by lazy {
        FitViewport(WORLD_WIDTH_WU, WORLD_HEIGHT_WU)
    }
    val stage: Stage by lazy {
        val result = Stage(gameViewport)
        Gdx.input.inputProcessor = result
        result
    }

    //    val stage: Stage by lazy { initStage() }
    val assets: AssetStorage by lazy { initAssetStorage() }
    val engine: Engine by lazy { initEngine() }


    override fun create() {
//        Gdx.app.logLevel = Application.LOG_DEBUG
        logger.info("Application :: START")

        val logStartTime = System.currentTimeMillis()
        logger.info("Application - Load Initialization assets :: START")

//        val asyncJobsForLoading = prepareLoadingForInitializationAssets()
//        KtxAsync.launch {
//            asyncJobsForLoading.joinAll()
//          }
        /* go to LoadingScreen to load remaining assets */
        addScreen(LoadingScreen(this@MainGame))
        setScreen<LoadingScreen>()
//        }


        logger.info("Application - Load Initialization assets " +
                ":: FINISH ## time = ${(System.currentTimeMillis() - logStartTime) * 0.001f} sec")
    }

    /** Init part */

    private fun initAssetStorage(): AssetStorage {
        KtxAsync.initiate()
        return AssetStorage()
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
//            addSystem(StartGameSystem().apply { setProcessing(false) })
            addSystem(ScreenInputSystem(gameViewport).apply { setProcessing(false) })
//            addSystem(MainStackSystem().apply { setProcessing(false) }) // TODO : SIS Even Applier (Maybe SIS -> TASK)
//            addSystem(DragCardSystem().apply { setProcessing(false) })  // TODO : SIS Even Applier
//            addSystem(StackBindingSystem().apply { setProcessing(false) })
//            addSystem(ReturnCardsSystem().apply { setProcessing(false) })
//            addSystem(CardPositionSystem().apply { setProcessing(false) })
            addSystem(TaskExecutorSystem().apply { setProcessing(false) })
//            addSystem(CalculateIsTouchableSystem().apply { setProcessing(false) })
            addSystem(RenderSystem(stage, gameViewport))
        }
    }


    companion object {
        private val logger by lazy { loggerApp<MainGame>() }
    }
}

