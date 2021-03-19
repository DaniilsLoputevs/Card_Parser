package towerdefense.ashley.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

class DebugSystem : EntitySystem() {
    lateinit var gameContext: GameContext
    lateinit var stacks: List<GameStackAdapter>
    lateinit var cards: List<GameCardAdapter>

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            println()
            println("DEBUG")
            println("context: hold card   = ${gameContext.touchingCard?.gameCardComp?.cardRank}")
            println("context: hold card z = ${gameContext.touchingCard?.transComp?.position?.z}")
//            println("gameContext entity = ${gameContext.touchingCard}")
//            println("gameContext status = ${gameContext.touchingCardStatus}")
            cards.forEach { println("Card: ${it.gameCardComp.cardRank.name} position = ${it.transComp.interpolatedPosition}") }
            println("DEBUG")
            println()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            println()
            println("DEBUG")
            var i = 0
            stacks.forEach { println("Stack ${it.transComp.interpolatedPosition} #${i++}   size = ${it.gameStackComp.size()}") }
//            println("context: hold card z = ${gameContext.touchingCard?.transComp?.position?.z}")
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
}