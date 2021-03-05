package towerdefense

import com.badlogic.ashley.core.*
import ktx.ashley.get

/**
 * For using in [EntitySystem].
 *
 * Ashley guarantees to as, that came entity into System HAVE REQUIREMENT components.
 * So, this function need for Smart Cast, cause Java API, Technically provide nullable return type,
 * but in real it never will happen.
 */
fun <T : Component> Entity.findComponent(mapper: ComponentMapper<T>) : T {
    val rslComponent = this[mapper]
    require(rslComponent != null) { "Entity |entity| must have a GraphicComponent. entity=$this" }
    return rslComponent
}


//fun Any?.ifNotNull(f: ()-> Unit){
//    if (this != null){
//        f()
//    }
//}

