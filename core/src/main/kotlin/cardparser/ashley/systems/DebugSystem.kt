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
            gameRep.cards.forEach(::println)
            println("DEBUG")
            println()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            println()
            println("DEBUG")
            var i = 0
            gameRep.stacks.forEach { println("#${i++} $it") }
//            println("context: hold card z = ${gameContext.touchingCard?.transComp?.position?.z}")
            println("DEBUG")
            println()
        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
//            println()
//            println("DEBUG")
//            GameEvent.BindingCards.cards.forEach { println("card :: $it")}
//            println("DEBUG")
//            println()
//        }
    }
}