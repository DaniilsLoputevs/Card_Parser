package cardparser.screens

import cardparser.MainGame
import cardparser.scenario.initKlondaikGame
import ktx.log.info

class GameScreen(
        game: MainGame
) : AbstractScreen(game) {
    private val logger = ktx.log.logger<GameScreen>()


    /**
     * Screen init Scenario: run all Scripts that make game game field:
     * -- create cards
     * -- create GameContext
     * -- prepare UI and etc...
     */
    init {
        logger.info { "Game Screen : Init Stage - START" }
        engine.initKlondaikGame(assets, gameViewport)
        logger.info { "Game Screen : Init Stage - START" }
    }

    override fun show() {
        logger.info { "Game Screen : Shown" }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        game.stage.viewport.update(width, height, true)
        game.gameViewport.update(width, height, true)
    }
}
