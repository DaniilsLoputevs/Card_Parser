package cardparser.ashley

import cardparser.ashley.components.GraphicComponent
import cardparser.ashley.components.TouchComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.ashley.components.klondike.GameCardComponent
import cardparser.ashley.components.klondike.GameStackComponent
import com.badlogic.ashley.core.*
import ktx.ashley.allOf
import ktx.ashley.get


/* ECS Engine */


private val GAME_STACK_FAMILY: Family = allOf(TransformComponent::class,
        GraphicComponent::class, GameStackComponent::class).get()
private val GAME_CARDS_FAMILY: Family = allOf(TransformComponent::class,
        GraphicComponent::class, GameCardComponent::class, TouchComponent::class).get()


fun Engine.getOurGameStacks(): List<GameStackAdapter> {
    return this.getEntitiesFor(GAME_STACK_FAMILY).toList().map { GameStackAdapter(it) }
}

fun Engine.getOurGameCards(): List<GameCardAdapter> {
    return this.getEntitiesFor(GAME_CARDS_FAMILY).toList().map { GameCardAdapter(it) }
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
inline fun <reified T : Component> Entity.findRequiredComponent(mapper: ComponentMapper<T>): T {
    val rslComponent = this[mapper]
    require(rslComponent != null) { "Entity |entity| must have a ${T::class.simpleName}. entity=$this" }
    return rslComponent
}

fun Entity.toPrint(): String {
    return this.components.toString()
}
