package towerdefense.ashley.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import towerdefense.ashley.components.GameCardComponent
import towerdefense.ashley.components.RemoveComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.event.GameEventManager
import towerdefense.gameStrucures.GameContext

class BindingSystem(
        private val gameEventManager: GameEventManager
) : IteratingSystem(allOf(GameCardComponent::class).exclude(RemoveComponent::class.java).get()) {
    lateinit var gameContext  : GameContext


    /**
     * Late implementation.
     */
    override fun processEntity(entity: Entity, deltaTime: Float) {

    }


    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
    }

}
