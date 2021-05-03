package cardparser.ashley.systems

import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import javax.swing.text.html.parser.Entity

class ReturnCardsSystem(var gameEventManager: GameEventManager) : EntitySystem(), GameEventListener {

    var dropEvent: GameEvent.DropEvent? = null;

    override fun update(deltaTime: Float) {
        dropEvent?.let { drop ->
            if (drop.cardList.size > 0) {
                drop.previousStack?.let { stack ->
                    stack.getCards().addAll(drop.cardList)
                    drop.cardList.clear()
                }
            }
            dropEvent = null
        }
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
}
