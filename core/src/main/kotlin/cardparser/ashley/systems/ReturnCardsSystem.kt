package cardparser.ashley.systems

import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem

class ReturnCardsSystem(val gameEventManager: GameEventManager)
    : EntitySystem(), GameEventListener {

    var dropEvent: GameEvent.DropEvent? = null

    override fun update(deltaTime: Float) {
        dropEvent?.let {
            if (it.cardList.size > 0 && it.cardReturn) {
                it.previousStack.addAll(it.cardList)
                it.cardList.clear()
                it.cardReturn = false
            }
        }
        dropEvent = null
    }


    override fun addedToEngine(engine: Engine?) {
        gameEventManager.addListener(GameEvent.DropEvent::class, this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameEventManager.removeListener(GameEvent.DropEvent::class, this)
        super.removedFromEngine(engine)
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DropEvent) {
            dropEvent = event
        }
    }

    companion object {
        private val logger = loggerApp<ReturnCardsSystem>()
    }
}
