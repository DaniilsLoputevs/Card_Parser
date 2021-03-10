package towerdefense.screens

import ktx.ashley.contains
import ktx.ashley.getSystem
import ktx.log.info
import towerdefense.MainGame
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.createStacks
import towerdefense.ashley.createTestCardDeck
import towerdefense.ashley.systems.BindingSystem
import towerdefense.ashley.systems.DebugSystem
import towerdefense.ashley.systems.ScreenInputSystem
import towerdefense.gameStrucures.DragAndDropManager
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
//        Gdx.input.inputProcessor = GameInputProcessor(engine, gameViewport, stage.batch)

        engine.run {

//            println("SYS")
//            engine.systems.forEach { println("$it :: ${it.checkProcessing()}") }
//            println("SYS")

            gameContext.cards = this.createTestCardDeck(game.assets)
            getSystem<DebugSystem>().apply {
                this.gameContext = this@GameScreen.gameContext
            }
            getSystem<ScreenInputSystem>().apply {
                this.inputProcessors = arrayOf(DragAndDropManager(gameContext, gameViewport))
                setProcessing(true)
            }
            getSystem<BindingSystem>().apply {
                this.gameContext = this@GameScreen.gameContext
                setProcessing(true)
            }
            gameContext.stacks = this.createStacks(game.assets)

//            println("SYS")
//            println(engine.systems)
//            engine.systems.forEach { println("$it :: ${it.checkProcessing()}") }
//            engine.systems.forEach { println("$it :: ${it.checkProcessing()}") }
//            println(engine.entities)

//            println("SYS")



//            getSystem<MoveSystem>().setProcessing(true)
//            getSystem<PlayerAnimationSystem>().setProcessing(true)
//            createPlayer(assets)
//            gameEventManager.dispatchEvent(GameEvent.PlayerSpawn)
//            createDarkMatter()
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