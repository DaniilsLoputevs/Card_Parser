package cardparser.screens

import cardparser.MainGame
import cardparser.logger.loggerApp
import cardparser.scenario.initKlondikeGame

class GameScreen(
        game: MainGame
) : AbstractScreen(game) {

    private val logger = loggerApp<GameScreen>()
    var chosen: String = "none"

    override fun show() {
        super.show()
        logger.info("Chosen $chosen")
        engine.initKlondikeGame(assets)
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
