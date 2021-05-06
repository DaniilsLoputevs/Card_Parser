package cardparser.ashley

import cardparser.ashley.components.GraphicComponent
import cardparser.ashley.components.TouchComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.ashley.components.GameCardComponent
import cardparser.ashley.components.GameStackComponent
import com.badlogic.ashley.core.*
import ktx.ashley.allOf
import ktx.ashley.get


/* ECS Engine */


private val GAME_STACK_FAMILY: Family = allOf(TransformComponent::class,
        GraphicComponent::class, GameStackComponent::class).get()
private val GAME_CARDS_FAMILY: Family = allOf(TransformComponent::class,
        GraphicComponent::class, GameCardComponent::class, TouchComponent::class).get()


fun Engine.getOurGameStacks(): List<GameStackAdapter> {
    return this.getEntitiesFor(GAME_STACK_FAMILY).toList().map { GameStackAdapter(it) }
}

fun Engine.getOurGameCards(): List<GameCardAdapter> {
    return this.getEntitiesFor(GAME_CARDS_FAMILY).toList().map { GameCardAdapter(it) }
}


/* ECS Entity */


/**
 * For using in [EntitySystem].
 *
 * Get component from *this*[Entity] that MUST BE IN, without possible null value or "not smart cast".
 *
 * Ashley guarantees to as, that came entity into System HAVE REQUIREMENT components.
 * So, this function need for Smart Cast, cause Java API, Technically provide nullable return type,
 * but in real it never will happen.
 */
inline fun <reified T : Component> Entity.findRequiredComponent(mapper: ComponentMapper<T>): T {
    val rslComponent = this[mapper]
    require(rslComponent != null) { "Entity |entity| must have a ${T::class.simpleName}. entity=$this" }
    return rslComponent
}

fun Entity.toPrint(): String {
    return this.components.toString()
}

enum class Logic {
    KLONDAIK {
        override fun doLogic(stack: GameStackAdapter, addedCards: List<GameCardAdapter>): Boolean {
            val cardComp = addedCards.first().gameCardComp;
            if (stack.getCards().isEmpty()) {
                return true
            }
            val stackComp = stack.getCards().last().gameCardComp
            return if (stackComp.cardSuit.colour != cardComp.cardSuit.colour) {
                if (stackComp.cardRank == GameCardComponent.CardRank.ACE) {
                    false
                } else if (stackComp.cardRank == GameCardComponent.CardRank.TWO && cardComp.cardRank == GameCardComponent.CardRank.ACE) {
                    true
                } else stackComp.cardRank.ordinal - 1 == cardComp.cardRank.ordinal
            } else {
                false
            }
        }
    },
    UPSTACKS {
        override fun doLogic(stack: GameStackAdapter, addedCards: List<GameCardAdapter>): Boolean {
            val cardComp = addedCards.last().gameCardComp;
            return if (stack.getCards().isEmpty() && cardComp.cardRank == GameCardComponent.CardRank.ACE)  {
                true
            } else if (stack.getCards().size > 0 && cardComp.cardSuit == stack.getCards().last().gameCardComp.cardSuit) {
                println(stack.getCards().last().gameCardComp)
                if (stack.getCards().last().gameCardComp.cardRank == GameCardComponent.CardRank.ACE &&
                    cardComp.cardRank == GameCardComponent.CardRank.TWO) {
                    true
                } else {
                    stack.getCards().last().gameCardComp.cardRank.ordinal + 1 == cardComp.cardRank.ordinal
                }
            } else {
                false
            }
        }
    };

    open fun doLogic(stack: GameStackAdapter, addedCards: List<GameCardAdapter>): Boolean = true
}

enum class CalculateLogic {
    KLONDAIK {
        override fun doLogic(stack: GameStackAdapter) {
            val cardsInStack = stack.getCards()
            cardsInStack.asReversed().forEachIndexed { index, card ->
                if (index == 0) {
                    card.touchComp.isTouchable = true
                    card.gameCardComp.isCardOpen = true
                } else {
                    val previousCard = cardsInStack.asReversed()[index - 1]
                    card.touchComp.isTouchable =
                        previousCard.touchComp.isTouchable
                                && card.gameCardComp.cardSuit.colour != previousCard.gameCardComp.cardSuit.colour
                                && (card.gameCardComp.cardRank.ordinal == previousCard.gameCardComp.cardRank.ordinal + 1
                                || (card.gameCardComp.cardRank == GameCardComponent.CardRank.TWO
                                && previousCard.gameCardComp.cardRank == GameCardComponent.CardRank.ACE))
                }
            }
        }
    };

    open fun doLogic(stack: GameStackAdapter): Unit {}
}
