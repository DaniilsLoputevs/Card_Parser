package cardparser.ashley

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import ktx.ashley.get

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

