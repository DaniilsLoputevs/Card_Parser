package cardparser.ashley.components.adapters

import com.badlogic.ashley.core.Entity

private val DEFAULT_ENTITY = Entity()

abstract class AbstractAdapter {
    var entity: Entity = DEFAULT_ENTITY
        set(value) {
            if (value === DEFAULT_ENTITY) return
            field = value
            refreshState()
        }

    abstract fun refreshState()
}