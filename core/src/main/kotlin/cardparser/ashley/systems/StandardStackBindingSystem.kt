package cardparser.ashley.systems

import cardparser.ashley.components.*
import cardparser.ashley.components.GameCardComponent.*
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude

class StandardStackBindingSystem(val gameEventManager: GameEventManager) : IteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class)
        .exclude(MainStackComponent::class, FoundationDragComponent::class).get()
), GameEventListener {

    var dropEvent: GameEvent.DropEvent? = null;

    override fun addedToEngine(engine: Engine?) {
        gameEventManager.addListener(GameEvent.DropEvent::class, this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameEventManager.removeListener(GameEvent.DropEvent::class, this)
        super.removedFromEngine(engine)
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        dropEvent = null
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        dropEvent?.let {
            val stack = GameStackAdapter(entity)
            if (stack.containsPos(it.position) && it.cardList.size > 0
                && checkIsPossibleAddCard(stack, it.cardList)
            ) {
                stack.getCards().addAll(it.cardList)
                it.cardList.clear()
                dropEvent = null
            }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DropEvent) {
            dropEvent = event
        }
    }

    private fun checkIsPossibleAddCard(stack: GameStackAdapter, addedCards: List<GameCardAdapter>): Boolean {
        val cardComp = addedCards.first().gameCardComp;
        if (stack.getCards().isEmpty()) {
            return true
        }
        val stackComp = stack.getCards().last().gameCardComp
        return if (stackComp.cardSuit.colour != cardComp.cardSuit.colour) {
            if (stackComp.cardRank == CardRank.ACE) {
                false
            } else if (stackComp.cardRank == CardRank.TWO && cardComp.cardRank == CardRank.ACE) {
                true
            } else stackComp.cardRank.ordinal - 1 == cardComp.cardRank.ordinal
        } else {
            false
        }
    }
}
