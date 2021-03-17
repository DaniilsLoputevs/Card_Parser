package towerdefense.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import towerdefense.gameStrucures.GameContext
import towerdefense.stubs.StubIteratingSystem

class DebugSystem : StubIteratingSystem() {
    lateinit var gameContext: GameContext

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            println("DEBUG")
            println("gameContext entity = ${gameContext.touchingCard}")
            println("gameContext status = ${gameContext.touchingCardStatus}")
            println("DEBUG")
            println()
        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
//            println("DEBUG")
////            println("gameContext stacks size  = ${gameContext.stacks.size}")
//            println("gameContext stacks one = ${gameContext.stacks[0]
//                    .findRequiredComponent(GameStackComponent.mapper)}")
//            println("gameContext stacks two = ${gameContext.stacks[1]
//                    .findRequiredComponent(GameStackComponent.mapper)}")
//            println("gameContext card     = ${gameContext.cards.size}")
//            println("gameContext card one = ${gameContext.cards[0]
//                    .findRequiredComponent(TransformComponent.mapper).position}")
//            println("gameContext card two = ${gameContext.cards[1]
//                    .findRequiredComponent(TransformComponent.mapper).position}")
//            println("DEBUG")
//            println()
//        }
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        TODO("Not yet implemented")
    }
}