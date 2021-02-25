package towerdefense.screens

//import ktx.scene2d.actors
//import ktx.scene2d.image
//import ktx.scene2d.label
//import ktx.scene2d.stack
//import ktx.scene2d.table
//import ktx.scene2d.textButton
import com.badlogic.gdx.scenes.scene2d.Stage
import towerdefense.asset.TextureAsset
import towerdefense.asset.TextureAtlasAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.info
import ktx.log.logger
import towerdefense.MainGame

/**
 * Screen for loading assert, init and setup requirement elements for app:
 * -- load asset(textures, music and etc...)
 * -- load, init and setup ui
 * -- prepare all others Screens for app
 */
class LoadingScreen(
    game: MainGame,
    private val stage: Stage = game.stage,
    private val assets: AssetStorage = game.assets
) : AbstractScreen(game) {
    private val logger = logger<LoadingScreen>()
 /*   UI things
    private val ACTOR_FADE_IN_TIME = 0.5f
    private val ACTOR_FADE_OUT_TIME = 1f
    private val OFFSET_TITLE_Y = 15f
    private val ELEMENT_PADDING = 7f
    private val MENU_ELEMENT_OFFSET_TITLE_Y = 20f

    private val bundle = assets[I18NBundleAsset.DEFAULT.descriptor]
    */

//    private lateinit var progressBar: Image
//    private lateinit var progressText: TextButton
//    private lateinit var touchToBegin: Label
    private var isLoadingFinish = false

    override fun show() {
        logger.info { "Loading Screen : Load assets - START" }
        loadMainAssets()
        !isLoadingFinish
        logger.info { "Loading Screen : Load assets - FINISH" }

//        game.addScreen(GameScreen(game))
//        game.addScreen(GameOverScreen(game))
//        game.addScreen(MenuScreen(game))

//        setupUI()
    }

    private fun loadMainAssets() {
        logger.info { "loading main assets" }
        val logStartTime = System.currentTimeMillis()
        val assetRefs = gdxArrayOf(
            TextureAtlasAsset.values().filter { !it.isSkinAtlas }.map { assets.loadAsync(it.descriptor) },
            TextureAsset.values().map { assets.loadAsync(it.descriptor) },
//            SoundAsset.values().map { assets.loadAsync(it.descriptor) },
//            ShaderProgramAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
            logger.debug { "loading main assets - Finish! It took: ${(System.currentTimeMillis() - logStartTime) * 0.001f}" }
//            assetsLoaded()
        }
    }

    override fun hide() {
        stage.clear()
    }


    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
//        if (assets.progress.isFinished && Gdx.input.justTouched() && game.containsScreen<MenuScreen>()) {
        if (assets.progress.isFinished && isLoadingFinish) {
//            game.removeScreen(LoadingScreen::class.java)
            game.removeScreen<LoadingScreen>()
            dispose()
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>()
        }

      renderProgressBar()
    }

    private fun renderProgressBar() {
//        progressBar.scaleX = assets.progress.percent
//        stage.run {
//            viewport.apply()
//            act()
//            draw()
//        }
    }

    override fun dispose() {
        logger.info { "Loading Screen : dispose" }
    }




//    private fun setupUI() {
//        stage.actors {
//            table {
//                defaults().fillX().expandX()
//
//                label(bundle["gameTitle"], SkinLabel.LARGE.name) { cell ->
//                    wrap = true
//                    setAlignment(Align.center)
//                    cell.apply {
//                        padTop(OFFSET_TITLE_Y)
//                        padBottom(MENU_ELEMENT_OFFSET_TITLE_Y)
//                    }
//                }
//                row()
//
//                touchToBegin = label(bundle["touchToBegin"], SkinLabel.LARGE.name) { cell ->
//                    wrap = true
//                    setAlignment(Align.center)
//                    color.a = 0f
//                    cell.padLeft(ELEMENT_PADDING).padRight(ELEMENT_PADDING).top().expandY()
//                }
//                row()
//
//                stack { cell ->
//                    progressBar = image(SkinImage.LIFE_BAR.atlasKey).apply {
//                        scaleX = 0f
//                    }
//                    progressText = textButton(bundle["loading"], SkinTextButton.LABEL_TRANSPARENT.name)
//                    cell.padLeft(ELEMENT_PADDING).padRight(ELEMENT_PADDING).padBottom(ELEMENT_PADDING)
//                }
//
//                top()
//                setFillParent(true)
//                pack()
//            }
//        }
//    }

    private fun assetsLoaded() {
//        game.addScreen(GameScreen(game))
//        game.addScreen(GameOverScreen(game))
//        game.addScreen(MenuScreen(game))
//        touchToBegin += forever(sequence(fadeIn(ACTOR_FADE_IN_TIME) + fadeOut(ACTOR_FADE_OUT_TIME)))
//        progressText.label.setText(bundle["loaded"])
    }
}
