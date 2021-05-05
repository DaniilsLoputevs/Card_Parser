package cardparser.ashley.systems

import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.MainStackComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude

class CalculateIsTouchableSystem(val gameEventManager: GameEventManager) : IteratingSystem(
        allOf(TransformComponent::class, GameStackComponent::class).exclude(MainStackComponent::class).get()
), GameEventListener {
    private val logger = loggerApp<CalculateIsTouchableSystem>()
    var dropEvent: GameEvent.DropEvent? = null
    var processed = false
    private val stack: GameStackAdapter = GameStackAdapter()


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
            stack.entity = entity
            val cardsInStack = stack.getCards()
            cardsInStack.asReversed().forEachIndexed { index, card ->
                if (index == 0) {
                    card.touchComp.isTouchable = true
                    card.gameCardComp.isCardOpen = true
                    logger.dev("---START STACK---")
                    logger.dev("index = ${index}, card = ${card.gameCardComp.cardRank}/${card.gameCardComp.cardSuit}")
                    logger.dev("card rank", card.gameCardComp.cardRank.ordinal)
                } else {
                    val previousCard = cardsInStack.asReversed()[index - 1]
                    logger.dev("card rank", card.gameCardComp.cardRank.ordinal)
                    logger.dev("previous card rank", previousCard.gameCardComp.cardRank.ordinal)
                    card.touchComp.isTouchable =
                            previousCard.touchComp.isTouchable &&
                                    card.gameCardComp.cardRank.ordinal == previousCard.gameCardComp.cardRank.ordinal + 1
                    //   || (card.gameCardComp.cardRank == GameCardComponent.CardRank.TWO
                    //       && previousCard.gameCardComp.cardRank == GameCardComponent.CardRank.ACE))
                    logger.dev("index = ${index}, card = ${card.gameCardComp.cardRank}/${card.gameCardComp.cardSuit}")
                    logger.dev("card rank", card.gameCardComp.cardRank.ordinal)
                    logger.dev("card touchable", card.touchComp.isTouchable)
                }
            }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DropEvent) {
            dropEvent = event
        }
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        gameEventManager.addListener(GameEvent.DropEvent::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        gameEventManager.removeListener(GameEvent.DropEvent::class, this)
    }
}
