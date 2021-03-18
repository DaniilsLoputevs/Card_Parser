package towerdefense.screens

import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.info
import ktx.log.logger
import towerdefense.MainGame
import towerdefense.asset.CardBackAtlas
import towerdefense.asset.CardDeckAtlas
import towerdefense.asset.GeneralAsset

/**
 * Screen for loading assert, init and setup requirement elements for app:
 * -- load asset(textures, music and etc...)
 * -- load, init and setup ui
 * -- prepare all others Screens for app
 */
class LoadingScreen(
        game: MainGame
) : AbstractScreen(game) {
    private val logger = logger<LoadingScreen>()

    /** custom async lock, - wait until assert will be full loaded */
    private var isAssetsLoadingFinish = false

    override fun show() {
        logger.info { "Loading Screen : Shown" }
        logger.info { "Loading Screen : Load Main assets - START" }
        val logStartTime = System.currentTimeMillis()
        loadMainAssets()

        logger.info { "Loading Screen : Load Main assets - FINISH ### load time: ${(System.currentTimeMillis() - logStartTime) * 0.001f} sec" }
    }

    private fun loadMainAssets() {
        val assetRefs = gdxArrayOf(
                CardDeckAtlas.values().map { assets.loadAsync(it.desc) },
                CardBackAtlas.values().map { assets.loadAsync(it.desc) },
                GeneralAsset.values().map { assets.loadAsync(it.desc) },
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
            isAssetsLoadingFinish = true
        }
    }

    override fun hide() {
        stage.clear()
    }


    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        game.gameViewport.update(width, height)
    }

    override fun render(delta: Float) {
        if (assets.progress.isFinished && isAssetsLoadingFinish) {
            game.removeScreen<LoadingScreen>()
            dispose()
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>()
        }
//        renderProgressBar()
    }

//    private fun renderProgressBar() {
//        progressBar.scaleX = assets.progress.percent
//        stage.run {
//            viewport.apply()
//            act()
//            draw()
//        }
//    }

    override fun dispose() {
        logger.info { "Loading Screen : disposed" }
    }

}
