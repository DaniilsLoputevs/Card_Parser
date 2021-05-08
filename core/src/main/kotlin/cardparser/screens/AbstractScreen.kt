package cardparser.screens

import cardparser.MainGame
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage

/**
 * Abstract base for any GameScreen
 */
abstract class AbstractScreen(
        val game: MainGame, // Singleton
        val stage: Stage = game.stage,
        val gameViewport: Viewport = game.gameViewport,
        val assets: AssetStorage = game.assets,
        var engine: Engine = game.engine,
        val gameEventManager: GameEventManager = game.gameEventManager
//    private val musicAsset: MusicAsset
) : KtxScreen
//    ,GameEventListener
{
//    private val gameViewport: Viewport = game.gameViewport
//    val stage: Stage = game.stage
//    val audioService: AudioService = game.audioService
//    val engine: Engine = game.engine
//    val gameEventManager: GameEventManager = game.gameEventManager
//    val assets: AssetStorage = game.assets
//    val bundle: I18NBundle = assets[I18NBundleAsset.DEFAULT.descriptor]

    /** Called when this screen becomes the current screen for a {@link Game}. */
    override fun show() {

//        LOG.debug { "Show ${this::class.simpleName}" }
//        val old = System.currentTimeMillis()
//        val music = assets.loadAsync(musicAsset.descriptor)
//        KtxAsync.launch {
//            music.join()
//            if (assets.isLoaded(musicAsset.descriptor)) {
//                // music was really loaded and did not get unloaded already by the hide function
//                LOG.debug { "It took ${(System.currentTimeMillis() - old) * 0.001f} seconds to load the $musicAsset music" }
//                audioService.play(musicAsset)
//            }
//        }
    }

    override fun hide() {
//        LOG.debug { "Hide ${this::class.simpleName}" }
//        stage.clear()
//        audioService.stop()
//        LOG.debug { "Number of entities: ${engine.entities.size()}" }
//        engine.removeAllEntities()
//        gameEventManager.removeListener(this)
//        KtxAsync.launch {
//            assets.unload(musicAsset.descriptor)
//        }
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        stage.viewport.update(width, height, true)
    }

//    override fun onEvent(event: GameEvent) = Unit
}
