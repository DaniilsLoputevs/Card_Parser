package cardparser.screens

import cardparser.MainGame
import cardparser.asset.CardBackAtlas
import cardparser.asset.CardDeckAtlas
import cardparser.asset.GeneralAsset
import cardparser.logger.loggerApp
import cardparser.utils.LabelStyles
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.scene2d.*

/**
 * Screen for loading assert, init and setup requirement elements for app:
 * -- load asset(textures, music and etc...)
 * -- load, init and setup ui
 * -- prepare all others Screens for app
 */
class LoadingScreen(
    game: MainGame
) : AbstractScreen(game) {
    private val logger = loggerApp<LoadingScreen>()
    private lateinit var touchToBeginLabel: Label

    override fun show() {
        val assetRefs = gdxArrayOf(
            CardDeckAtlas.values().map { assets.loadAsync(it.desc) },
            CardBackAtlas.values().map { assets.loadAsync(it.desc) },
            GeneralAsset.values().map { assets.loadAsync(it.desc) },
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
            addGameScreen()
        }
        showLoadingUI()
    }


    private fun showLoadingUI() {
        stage.actors {
            table {
                touchToBeginLabel = label("Touch to begin", LabelStyles.WHITE32.name) {
                    wrap = true
                    setAlignment(Align.center)
                    color.a = 0f
                }
                row()
                setFillParent(true)
                pack()
            }
        }
        stage.isDebugAll = false
    }

    private fun addGameScreen() {
        game.addScreen(GameScreen(game))
        touchToBeginLabel += Actions.forever(Actions.fadeIn(1f) + Actions.fadeOut(1f))
    }

    override fun render(delta: Float) {
        if (assets.progress.isFinished && Gdx.input.justTouched() && game.containsScreen<GameScreen>()) {
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
            dispose()
        }
        stage.run {
            viewport.apply()
            act()
            draw()
        }
    }

    override fun dispose() {
        super.dispose()
        logger.info { "Loading Screen :: DISPOSED" }
    }
}




