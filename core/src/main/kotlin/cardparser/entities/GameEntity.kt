package cardparser.entities

import cardparser.ashley.components.*
import cardparser.ashley.findComp
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Entity

abstract class GameEntity : TransformAPI {
    var entity: Entity = DEFAULT_ENTITY
        set(value) {
            if (value == DEFAULT_ENTITY) {
                logger.warm("Try to set EMPTY entity to ${GameEntity::class.simpleName}.entity"); return
            }
            field = value
            refreshComponentsLinks()
        }

    private fun refreshComponentsLinks() {
        this.transComp = entity.findComp(TransformComp.mapper)
        if (this is CardAPI) this.cardComp = entity.findComp(CardComp.mapper)
        if (this is MainStackAPI) this.mainStackComp = entity.findComp(MainStackComp.mapper)
        if (this is StackAPI) this.stackComp = entity.findComp(StackComp.mapper)
        if (this is TouchAPI) this.touchComp = entity.findComp(TouchComp.mapper)
    }

    companion object {
        private val DEFAULT_ENTITY = Entity()
        private val logger = loggerApp<GameEntity>()
    }
}
