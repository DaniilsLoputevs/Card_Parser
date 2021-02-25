package towerdefense.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import ktx.app.KtxScreen
import towerdefense.MainGame

/** First screen of the application. Displayed after the application is created.  */
class FirstScreen(game: MainGame) : AbstractScreen(game) {


    override fun show() {
//        LOG.debug { "Show" }
//
//        val old = System.currentTimeMillis()
//        val assetRefs = gdxArrayOf(
//            TextureAtlasAsset.values().filter { !it.isSkinAtlas }.map { assets.loadAsync(it.descriptor) },
//            TextureAsset.values().map { assets.loadAsync(it.descriptor) },
//            SoundAsset.values().map { assets.loadAsync(it.descriptor) },
//            ShaderProgramAsset.values().map { assets.loadAsync(it.descriptor) }
//        ).flatten()
//        KtxAsync.launch {
//            assetRefs.joinAll()
//            LOG.debug { "It took ${(System.currentTimeMillis() - old) * 0.001f} seconds to load assets and initialize" }
//            assetsLoaded()
//        }
//
//        setupUI()
    }


//    override fun render(delta: Float) {
//        if (assets.progress.isFinished && Gdx.input.justTouched() && game.containsScreen<MenuScreen>()) {
//            game.removeScreen(LoadingScreen::class.java)
//            dispose()
//            game.setScreen<MenuScreen>()
//        }
//
//        progressBar.scaleX = assets.progress.percent
//        stage.run {
//            viewport.apply()
//            act()
//            draw()
//        }
//    }
//
//    override fun hide() {
//        stage.clear()
//    }
}