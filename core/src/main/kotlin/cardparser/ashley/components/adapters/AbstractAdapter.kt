package cardparser.ashley.components.adapters

import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Entity

private val DEFAULT_ENTITY = Entity()

private val logger = loggerApp<AbstractAdapter>()

abstract class AbstractAdapter {
    var entity: Entity = DEFAULT_ENTITY
        set(value) {
            if (value == DEFAULT_ENTITY) {
                logger.warm("Try to set EMPTY entity to AbstractAdapter.entity");return
            }
            field = value
            refreshState()
        }

    abstract fun refreshState()
}
