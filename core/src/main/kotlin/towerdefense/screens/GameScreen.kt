package towerdefense.screens

import ktx.ashley.getSystem
import ktx.log.info
import towerdefense.MainGame
import towerdefense.ashley.engine.createStacksDefault
import towerdefense.ashley.engine.initGameDefault
import towerdefense.ashley.getOurGameCards
import towerdefense.ashley.getOurGameStacks
import towerdefense.ashley.systems.CardBindingSystem
import towerdefense.ashley.systems.DebugSystem
import towerdefense.ashley.systems.RenderSystem
import towerdefense.ashley.systems.ScreenInputSystem
import towerdefense.asset.CardBackAtlas
import towerdefense.asset.GeneralAsset
import towerdefense.gameStrucures.CardMoveProcessor
import towerdefense.gameStrucures.GameContext

class GameScreen(
        game: MainGame
) : AbstractScreen(game) {
    private val logger = ktx.log.logger<GameScreen>()
    private val gameContext = GameContext()


    /**
     * Screen init Scenario: run all Scripts that make game game field:
     * -- create cards
     * -- create GameContext
     * -- prepare UI and etc...
     */
    init {
        logger.info { "Game Screen : Init Stage - START" }
        engine.initGameDefault(game.assets, gameContext, gameViewport)
        logger.info { "Game Screen : Init Stage - START" }
    }

    override fun show() {
        logger.info { "Game Screen : Shown" }
    }

    override fun render(delta: Float) {
//        println("GameScreen :: render invoke time")
//        println("DEV")
//        println("is Touch: ${Gdx.input.isTouched}")
//        println("is inputProcessor: ${Gdx.input.inputProcessor.touchDragged()}")
//        println("DEV")

        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        game.stage.viewport.update(width, height, true)
        game.gameViewport.update(width, height, true)
    }
}