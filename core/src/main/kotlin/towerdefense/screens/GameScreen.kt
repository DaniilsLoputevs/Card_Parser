package towerdefense.screens

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import towerdefense.ashley.systems.RenderSystem
import ktx.ashley.getSystem
import ktx.log.info
import towerdefense.MainGame
import towerdefense.ashley.createTestCardBack
import towerdefense.stubs.InProc

class GameScreen(
    game: MainGame
) : AbstractScreen(game) {
    private val logger = ktx.log.logger<GameScreen>()
    private val renderSystem = game.engine.getSystem<RenderSystem>()

//    private  val dnd : DragAndDrop = DragAndDrop()
    private lateinit var stacks : Array<Entity>

    init {
        logger.info { "Game Screen : Init Stage" }
        Gdx.graphics.setVSync(false)
        Gdx.input.inputProcessor = InProc(engine.entities, gameViewport)
//        engine.

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
        renderSystem.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        game.stage.viewport.update(width, height, true)
        game.gameViewport.update(width, height, true)
    }
}