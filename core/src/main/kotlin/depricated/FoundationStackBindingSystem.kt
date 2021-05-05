package depricated

import cardparser.ashley.components.*
import cardparser.ashley.components.GameCardComponent.*
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude

@Deprecated("")
class FoundationStackBindingSystem(val gameEventManager: GameEventManager): IteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class, FoundationStackComponent::class)
        .exclude(MainStackComponent::class).get()), GameEventListener {

    var dropEvent: GameEvent.DropEvent? = null

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
            val adapter = GameStackAdapter(entity)
            if (adapter.containsPosStack(it.position) && it.cardList.size == 1 && checkIsPossibleAddCard(adapter, it.cardList)) {
                adapter.getCards().addAll(it.cardList)
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
        val cardComp = addedCards.last().gameCardComp;
        return if (stack.getCards().isEmpty() && cardComp.cardRank == CardRank.ACE)  {
            true
        } else if (stack.getCards().size > 0 && cardComp.cardSuit == stack.getCards().last().gameCardComp.cardSuit) {
            println(stack.getCards().last().gameCardComp)
            if (stack.getCards().last().gameCardComp.cardRank == CardRank.ACE &&
                cardComp.cardRank == CardRank.TWO) {
                true
            } else {
                stack.getCards().last().gameCardComp.cardRank.ordinal + 1 == cardComp.cardRank.ordinal
            }
        } else {
            false
        }
    }
}
