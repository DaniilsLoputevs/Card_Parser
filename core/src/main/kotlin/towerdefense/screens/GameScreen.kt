package towerdefense.screens

import towerdefense.ashley.systems.MoveSystem
import towerdefense.ashley.systems.RenderSystem
import towerdefense.event.GameEvent
import ktx.ashley.getSystem
import ktx.log.info
import towerdefense.MainGame

class GameScreen(
    game: MainGame
) : AbstractScreen(game) {
    private val logger = ktx.log.logger<GameScreen>()
    private val renderSystem = game.engine.getSystem<RenderSystem>()

    override fun show() {
        logger.info { "Game Screen : Shown" }
        engine.run {
            // remove any power ups and reset the spawn timer

            getSystem<MoveSystem>().setProcessing(true)
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
}