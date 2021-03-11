package towerdefense.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.ashley.allOf
import ktx.ashley.oneOf
import towerdefense.ashley.components.DragAndDropComponent
import towerdefense.ashley.components.GraphicComponent
import towerdefense.ashley.components.MoveComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.game.GameStacksComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.ashley.toPrint
import towerdefense.gameStrucures.GameContext

class DebugSystem
    : IteratingSystem(
        oneOf(GameCardComponent::class, GameStacksComponent::class, DragAndDropComponent::class,
                GraphicComponent::class, MoveComponent::class, TransformComponent::class
        ).get()){
    lateinit var gameContext: GameContext

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            println("DEBUG")
            println("gameContext entity = ${gameContext.dndSelectedEntity}")
            println("gameContext status = ${gameContext.dndEntityStatus}")
            println("DEBUG")
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            println("DEBUG")
//            println("gameContext stacks size  = ${gameContext.stacks.size}")
            println("gameContext stacks one = ${gameContext.stacks[0]
                    .findRequiredComponent(GameStacksComponent.mapper)}")
            println("gameContext stacks two = ${gameContext.stacks[1]
                    .findRequiredComponent(GameStacksComponent.mapper)}")
            println("DEBUG")
        }
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        TODO("Not yet implemented")
    }
}