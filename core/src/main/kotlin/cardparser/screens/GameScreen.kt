package cardparser.screens

import cardparser.MainGame
import cardparser.logger.logger
import cardparser.scenario.initKlondaikGame

class GameScreen(
        game: MainGame
) : AbstractScreen(game) {
    private val logger = logger<GameScreen>()


    /**
     * Screen init Scenario: run all Scripts that make game game field:
     * -- create cards
     * -- create GameContext
     * -- prepare UI and etc...
     */
    init {
        logger.info("Game init :: START")
        engine.initKlondaikGame(assets, gameViewport)
        logger.info("Game init :: FINISH")
    }

    override fun render(delta: Float) = engine.update(delta)

    override fun resize(width: Int, height: Int) {
        game.stage.viewport.update(width, height, true)
        game.gameViewport.update(width, height, true)
    }
}
