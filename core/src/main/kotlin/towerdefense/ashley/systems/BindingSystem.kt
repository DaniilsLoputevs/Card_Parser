package towerdefense.ashley.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import towerdefense.CARD_STACK_OFFSET
import towerdefense.ashley.components.RemoveComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.game.GameStackComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.gameStrucures.DragAndDropManager.DragAndDropStatus.DROPPED
import towerdefense.gameStrucures.DragAndDropManager.DragAndDropStatus.NONE
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

class BindingSystem
    : IteratingSystem(allOf(GameCardComponent::class).exclude(RemoveComponent::class.java).get()) {
    lateinit var gameContext: GameContext

    /**
     * just for optimization, only for it.
     * buffer that using like the container for get and hold hitbox center of newCard in:
     * applyCardToNotEmptyStack(...)
     */
    private val newCardCenter = Vector2(-1f, -1f)

    /* Methods */

    override fun update(deltaTime: Float) {
        if (gameContext.dndSelectedEntity != null
                && gameContext.dndEntityStatus == DROPPED) {
            processEntity(gameContext.dndSelectedEntity!!, deltaTime)
            gameContext.dndEntityStatus = NONE
            gameContext.dndSelectedEntity = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        var cardApplyIntoNewStack = false
//        println("PROCC - START")
        for (stack in gameContext.stacks) {
            val stackComp = stack.findRequiredComponent(GameStackComponent.mapper)
//            println("APPLY stack ${i++} - ${if (stackComp.isEmpty()) "EMPTY" else "NOT EMPTY"}")

//            println("FUG B")
//            println("gameContext stacks one = ${gameContext.stacks[0]
//                    .findRequiredComponent(GameStackComponent.mapper)}")
//            println("gameContext stacks two = ${gameContext.stacks[1]
//                    .findRequiredComponent(GameStackComponent.mapper)}")
//            println(cardApplyIntoNewStack)
//            println("FUG B")

            cardApplyIntoNewStack = when (stackComp.isEmpty()) {
                true -> applyCardToEmptyStack(entity, stack, stackComp)
                false -> applyCardToNotEmptyStack(entity, stackComp)
            }
//            println("FUG")
//            println("gameContext stacks one = ${gameContext.stacks[0]
//                    .findRequiredComponent(GameStackComponent.mapper)}")
//            println("gameContext stacks two = ${gameContext.stacks[1]
//                    .findRequiredComponent(GameStackComponent.mapper)}")
//            println(cardApplyIntoNewStack)
//            println("FUG")
//            println("APPLY ${if (stackComp.isEmpty()) "EMPTY" else "NOT EMPTY"}")
//            println(i++)
            if (cardApplyIntoNewStack) break
        }
//        println("PROCC - END")
//        println()
        if (!cardApplyIntoNewStack) {
            returnGameCardToStack(entity)
//                gameContext.dndEntityStatus = BINDING_FAIL
        }
    }

    private fun applyCardToEmptyStack(entity: Entity, stack: Entity, stackStackComp: GameStackComponent): Boolean {

        val entityTransComp = entity.findRequiredComponent(TransformComponent.mapper)
        val stackTransComp = stack.findRequiredComponent(TransformComponent.mapper)

//        if (newCard != stackLastCard) return false
        println("EMPTY contains = ${stackTransComp.shape.contains(entityTransComp.shape.getCenter(newCardCenter))}")
        if (stackTransComp.shape.contains(entityTransComp.shape.getCenter(newCardCenter))) {
            val entityGameCardComp = entity.findRequiredComponent(GameCardComponent.mapper)

            println("UNBOUND - EMPTY")
            unbindCardFromStack(entity)
            stackStackComp.addGameCard(entity)

            val newPos = Vector2(
                    stackTransComp.interpolatedPosition.x,
                    stackTransComp.interpolatedPosition.y,
            )
            entityTransComp.setTotalPosition(newPos)
            entityGameCardComp.moveNextCards(newPos)
            return true
        } else return false
    }

    private fun applyCardToNotEmptyStack(entity: Entity, stackComp: GameStackComponent): Boolean {
        val newCard = GameCardAdapter(entity)
        val stackLastCard = GameCardAdapter(stackComp.getLastCard())
        newCard.transfComp.shape.getCenter(newCardCenter)

        // check: is it try to add card to current stack.
        // we can't add card to stack, what already contains this card.
        if (newCard == stackLastCard) return false

        if (stackLastCard.transfComp.shape.contains(newCardCenter)) {

            // check: can we add card to current stack(stack last card)
            if (!stackLastCard.gameCardComp.setNextPredicate.test(newCard.gameCardComp)) return false

            unbindCardFromStack(entity)
            stackLastCard.gameCardComp.next = entity
            stackComp.addGameCard(entity)

            val newPos = Vector2(
                    stackLastCard.transfComp.interpolatedPosition.x,
                    stackLastCard.transfComp.interpolatedPosition.y - CARD_STACK_OFFSET,
            )
            newCard.transfComp.setTotalPosition(newPos)
            newCard.gameCardComp.moveNextCards(newPos)
            return true
        } else return false
    }

    /**
     * Unbinding card from it's stack.
     */
    private fun unbindCardFromStack(card: Entity) {
        for (eachStack in gameContext.stacks) {
            val stack = eachStack.findRequiredComponent(GameStackComponent.mapper)
            if (stack.contains(card)) {
                stack.removeGameCard(card)
                return
            }
        }
    }

    private fun returnGameCardToStack(cardEntity: Entity) {
        for (eachStack in gameContext.stacks) {
            val stack = GameStackAdapter(eachStack)

            if (stack.gameStackComp.contains(cardEntity)) {
                val offsetY = stack.gameStackComp.size() + CARD_STACK_OFFSET
                val newPos = Vector2(
                        stack.transfComp.interpolatedPosition.x,
                        stack.transfComp.interpolatedPosition.y - offsetY,
                )
                GameCardAdapter(cardEntity).apply {
                    transfComp.setTotalPosition(newPos)
                    gameCardComp.moveNextCards(newPos)
                }
                return
            }
        }
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
    }

}
