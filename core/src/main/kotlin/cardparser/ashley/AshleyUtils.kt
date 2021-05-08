package cardparser.ashley

import cardparser.ashley.components.*
import cardparser.ashley.objects.Card
import cardparser.ashley.objects.Stack
import com.badlogic.ashley.core.*
import ktx.ashley.allOf
import ktx.ashley.get


/* ECS Engine */


private val GAME_STACK_FAMILY: Family = allOf(TransformComponent::class,
        GraphicComponent::class, GameStackComponent::class).get()
private val GAME_CARDS_FAMILY: Family = allOf(TransformComponent::class,
        GraphicComponent::class, GameCardComponent::class, TouchComponent::class).get()


fun Engine.getOurGameStacks(): List<Stack> {
    return this.getEntitiesFor(GAME_STACK_FAMILY).toList().map { Stack(it) }
}

fun Engine.getOurGameCards(): List<Card> {
    return this.getEntitiesFor(GAME_CARDS_FAMILY).toList().map { Card(it) }
}


/* ECS Entity */


/**
 * For using in [EntitySystem].
 *
 * Get component from *this*[Entity] that MUST BE IN, without possible null value or "not smart cast".
 *
 * Ashley guarantees to as, that came entity into System HAVE REQUIREMENT components.
 * So, this function need for Smart Cast, cause Java API, Technically provide nullable return type,
 * but in real it never will happen.
 */
inline fun <reified T : Component> Entity.findComp(mapper: ComponentMapper<T>): T {
    val rslComponent = this[mapper]
    require(rslComponent != null) { "Entity |entity| must have a ${T::class.simpleName}. entity=$this" }
    return rslComponent
}

fun Entity.toPrint(): String {
    return this.components.toString()
}

