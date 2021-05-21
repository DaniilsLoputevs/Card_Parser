package cardparser.screens

import cardparser.MainGame
import cardparser.logger.loggerApp
import cardparser.scenario.initKlondikeGame

class GameScreen(
        game: MainGame
) : AbstractScreen(game) {


    /**
     * Screen init Scenario: run all Scripts that make game game field:
     * -- create cards
     * -- create GameContext
     * -- prepare UI and etc...
     */
    init {
        val logStartTime = System.currentTimeMillis()
        logger.info("Game init :: START")
        engine.initKlondikeGame(assets, gameViewport)
        logger.info("Game init :: FINISH ## time = ${(System.currentTimeMillis() - logStartTime) * 0.001f} sec")
    }

    override fun render(delta: Float) = engine.update(delta)

    override fun resize(width: Int, height: Int) {
        game.stage.viewport.update(width, height, true)
        game.gameViewport.update(width, height, true)
    }

    companion object {
        private val logger by lazy { loggerApp<GameScreen>() }
    }
}
