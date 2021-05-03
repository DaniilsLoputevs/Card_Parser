package cardparser.ashley.systems

import cardparser.ashley.components.*
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude

class CalculateIsTouchableSystem(val gameEventManager: GameEventManager) : IteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class)
        .exclude(MainStackComponent::class, FoundationDragComponent::class).get()
), GameEventListener {

    var dropEvent: GameEvent.DropEvent? = null
    var processed = false

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        gameEventManager.addListener(GameEvent.DropEvent::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        gameEventManager.removeListener(GameEvent.DropEvent::class, this)
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if (processed) {
            processed = false
            dropEvent = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        dropEvent?.let {
            processed = true
            val cardsInStack = GameStackAdapter(entity).getCards()
            cardsInStack.asReversed().forEachIndexed { index, card ->
                if (index == 0) {
                    card.touchComp.isTouchable = true
                    card.gameCardComp.isCardOpen = true
                } else {
                    val previousCard = cardsInStack[index - 1]
                    card.touchComp.isTouchable =
                        previousCard.touchComp.isTouchable
                        //    && (card.gameCardComp.cardRank.ordinal + 1 == previousCard.gameCardComp.cardRank.ordinal
                        //    || (card.gameCardComp.cardRank == GameCardComponent.CardRank.TWO
                        //        && previousCard.gameCardComp.cardRank == GameCardComponent.CardRank.ACE))
                }
            }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DropEvent) {
            dropEvent = event
        }
    }
}
