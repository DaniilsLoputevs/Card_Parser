package towerdefense.screens

import ktx.ashley.getSystem
import ktx.log.info
import towerdefense.MainGame
import towerdefense.ashley.engine.createStacksDefault
import towerdefense.ashley.engine.initGameDefault
import towerdefense.ashley.systems.CardBindingSystem
import towerdefense.ashley.systems.DebugSystem
import towerdefense.ashley.systems.ScreenInputSystem
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
        logger.info { "Game Screen : Init Stage" }

        engine.run {
            this.initGameDefault(gameContext, game.assets)

            getSystem<DebugSystem>().apply {
                this.gameContext = this@GameScreen.gameContext
            }
            getSystem<ScreenInputSystem>().apply {
                this.inputProcessors = arrayOf(CardMoveProcessor(gameContext, gameViewport))
                setProcessing(true)
            }
            getSystem<CardBindingSystem>().apply {
                this.gameContext = this@GameScreen.gameContext
                setProcessing(true)
            }
            this.createStacksDefault(game.assets)
        }
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