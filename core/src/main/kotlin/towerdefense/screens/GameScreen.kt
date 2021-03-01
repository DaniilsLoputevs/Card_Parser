package towerdefense.screens

import towerdefense.ashley.systems.MoveSystem
import towerdefense.ashley.systems.RenderSystem
import towerdefense.event.GameEvent
import ktx.ashley.getSystem
import ktx.log.info
import towerdefense.MainGame
import towerdefense.ashley.createTestCardBack

class GameScreen(
    game: MainGame
) : AbstractScreen(game) {
    private val logger = ktx.log.logger<GameScreen>()
    private val renderSystem = game.engine.getSystem<RenderSystem>()

    override fun show() {
        logger.info { "Game Screen : Shown" }
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

    override fun render(delta: Float) {
//        println("GameScreen :: render invoke time")
        renderSystem.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        game.stage.viewport.update(width, height, true)
        game.gameViewport.update(width, height, true)
    }
}