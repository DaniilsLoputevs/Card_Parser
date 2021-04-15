package cardparser.ashley.systems

import cardparser.gameStrucures.GameRepository
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class DebugSystem : EntitySystem() {
    lateinit var gameRep: GameRepository

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            println()
            println("DEBUG")
            gameRep.cards.forEach {
                println("all cards:  ${it.gameCardComp.cardRank.name} position = ${it.transComp.position}")
            }
//            GameContext.touchList.forEach {
//                println("hold cards: ${it.gameCardComp.cardRank}")
//            }
            println("DEBUG")
            println()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            println()
            println("DEBUG")
            var i = 0
            gameRep.stacks.forEach { println("Stack ${it.transComp.position} #${i++}   size = ${it.gameStackComp.size()}") }
//            println("context: hold card z = ${gameContext.touchingCard?.transComp?.position?.z}")
            println("DEBUG")
            println()
        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
//            println()
//            println("DEBUG")
//            println("touch list: status = ${GameContext.touchListStatus.name}")
//            println("touch list: size   = ${GameContext.touchList.size}")
//            println("DEBUG")
//            println()
//        }
    }
}