package cardparser.ashley.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class DebugSystem : EntitySystem() {


    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            println()
            println("DEBUG")
            println("DEBUG")
            println()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            println()
            println("DEBUG")
            var i = 0
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
