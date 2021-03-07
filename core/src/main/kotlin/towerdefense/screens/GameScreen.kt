package towerdefense.screens

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import towerdefense.ashley.systems.RenderSystem
import ktx.ashley.getSystem
import ktx.log.info
import towerdefense.MainGame
import towerdefense.ashley.createTestCardBack
import towerdefense.input.GameInputProcessor

class GameScreen(
    game: MainGame
) : AbstractScreen(game) {
    private val logger = ktx.log.logger<GameScreen>()
    private val renderSystem = game.engine.getSystem<RenderSystem>()

//    private  val dnd : DragAndDrop = DragAndDrop()
    private lateinit var stacks : Array<Entity>

    /**
     * Screen init Scenario: run all Scripts that make game game field:
     * -- create cards
     * -- create GameContext
     * -- prepare UI and etc...
     */
    init {
        logger.info { "Game Screen : Init Stage" }
        Gdx.input.inputProcessor = GameInputProcessor(engine, gameViewport, stage.batch)

        engine.run {
            // remove any power ups and reset the spawn timer

            createTestCardBack(game.assets)

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

        renderSystem.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        game.stage.viewport.update(width, height, true)
        game.gameViewport.update(width, height, true)
    }
}