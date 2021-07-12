package cardparser.screens

import cardparser.MainGame
import cardparser.asset.CardBackAtlas
import cardparser.asset.CardDeckAtlas
import cardparser.asset.GeneralAsset
import cardparser.logger.loggerApp
import cardparser.utils.ImageButtonStyle
import cardparser.utils.LabelStyles
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.app.clearScreen
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.scene2d.*
import javax.swing.text.StyleConstants.setAlignment

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
    private lateinit var loadingLabel: Label
    private lateinit var touchToBeginLabel: Label

    /** turn off to see GUI */
    private val justStart = false

    override fun show() {
        val assetRefs = gdxArrayOf(
            CardDeckAtlas.values().map { assets.loadAsync(it.desc) },
            CardBackAtlas.values().map { assets.loadAsync(it.desc) },
            GeneralAsset.values().map { assets.loadAsync(it.desc) },
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
            game.addScreen(MenuScreen(game))
            loadingLabel.color.a = 0f
            touchToBeginLabel += Actions.forever(Actions.fadeIn(0.5f) + Actions.fadeOut(0.5f))
        }
        showLoadingUI()
    }


    private fun showLoadingUI() {
        stage.clear()
        stage.actors {
            table {
                stack {
                    loadingLabel = label("Loading...", LabelStyles.WHITE46_BI.name) {
                        setAlignment(Align.center)
                    }
                    touchToBeginLabel = label("Touch to start...", LabelStyles.WHITE46_BI.name) {
                        setAlignment(Align.center)
                        color.a = 0f
                    }
                }

                setFillParent(true)
                pack()
            }
        }
        stage.isDebugAll = false
    }

    override fun render(delta: Float) {
        if (assets.progress.isFinished && (Gdx.input.isKeyPressed(Input.Keys.S) || justStart)) {
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
        }
        if (assets.progress.isFinished && Gdx.input.justTouched() && game.containsScreen<MenuScreen>()) {
            game.setScreen<MenuScreen>()
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




