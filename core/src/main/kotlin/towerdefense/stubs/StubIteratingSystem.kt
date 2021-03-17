package towerdefense.stubs

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import towerdefense.ashley.components.RemoveComponent

open class StubIteratingSystem : IteratingSystem(
        allOf()
        .exclude(RemoveComponent::class.java).get()
) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        System.err.println("StubIteratingSystem :: processEntity  ##  invoked!!! Please check your code!")
    }
}