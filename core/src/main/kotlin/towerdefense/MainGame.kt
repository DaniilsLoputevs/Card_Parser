package towerdefense

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import towerdefense.asset.TextureAtlasAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.logger
import towerdefense.screens.LoadingScreen

/**
 * Main Game class
 */
class MainGame : KtxGame<KtxScreen>() {

    private val logger = logger<Game>()
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val stage: Stage by lazy { initStage() }
    val assets: AssetStorage by lazy { initAssetStorage() }
    val engine: Engine by lazy { initEngine() }


    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
//        logger.debug { "Main - create method #START#" }

        loadRequiredBasicAssets()

        addScreen(LoadingScreen(this@MainGame))
        setScreen<LoadingScreen>()
    }


    /** Init part */

    private fun initStage(): Stage {
        val result = Stage(FitViewport(V_WIDTH_PIXELS.toFloat(), V_HEIGHT_PIXELS.toFloat()))
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
    private fun loadRequiredBasicAssets() {

        /* load skin and go to LoadingScreen for remaining asset loading*/
//        var old = System.currentTimeMillis()
        val assetRefs = gdxArrayOf(
            TextureAtlasAsset.values().filter { it.isSkinAtlas }.map { assets.loadAsync(it.descriptor) },
//            BitmapFontAsset.values().map { assets.loadAsync(it.descriptor) },
//            I18NBundleAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
//            logger.debug { "It took ${(System.currentTimeMillis() - old) * 0.001f} seconds to load skin assets" }
//            old = System.currentTimeMillis()

            /*skin assets loaded -> create skin*/
//            createSkin(assets) // it - work with skin texture, - maybe future
//            logger.debug { "It took ${(System.currentTimeMillis() - old) * 0.001f} seconds to create the skin" }

            /*go to LoadingScreen to load remaining assets*/
//            addScreen(LoadingScreen(this@Game))
//            setScreen<LoadingScreen>()
        }
    }

    private fun initEngine(): Engine {
        return PooledEngine().apply {
            val atlas = assets[TextureAtlasAsset.GRAPHICS.descriptor]

//            addSystem(DebugSystem(gameEventManager, audioService))
//            addSystem(PowerUpSystem(gameEventManager, audioService).apply {
//                setProcessing(false)
//            })
//            addSystem(PlayerInputSystem(gameViewport))
//            addSystem(MoveSystem(gameEventManager).apply {
//                setProcessing(false)
//            })
//            addSystem(DamageSystem(gameEventManager, audioService))
//            addSystem(
//                PlayerAnimationSystem(
//                    atlas.findRegion("ship_base"),
//                    atlas.findRegion("ship_left"),
//                    atlas.findRegion("ship_right")
//                ).apply {
//                    setProcessing(false)
//                }
//            )
//            addSystem(AttachSystem())
//            addSystem(AnimationSystem(atlas))
//            addSystem(CameraShakeSystem(gameViewport.camera, gameEventManager))
//            addSystem(PlayerColorSystem(gameEventManager))
//            addSystem(
//                RenderSystem(
//                    stage,
//                    assets[ShaderProgramAsset.OUTLINE.descriptor],
//                    gameViewport,
//                    gameEventManager,
//                    assets[TextureAsset.BACKGROUND.descriptor]
//                )
//            )
//            addSystem(RemoveSystem(gameEventManager))
//        }
//
        }
    }

}

