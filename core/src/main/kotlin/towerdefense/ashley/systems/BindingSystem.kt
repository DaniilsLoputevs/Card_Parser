package towerdefense.ashley.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import towerdefense.CARD_STACK_OFFSET
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.RemoveComponent
import towerdefense.ashley.components.TransformComponent
import towerdefense.ashley.components.game.GameStacksComponent
import towerdefense.ashley.findRequiredComponent
import towerdefense.gameStrucures.DragAndDropManager.DragAndDropStatus.*
import towerdefense.gameStrucures.GameCardAdapter
import towerdefense.gameStrucures.GameContext

class BindingSystem
    : IteratingSystem(allOf(GameCardComponent::class).exclude(RemoveComponent::class.java).get()) {
    lateinit var gameContext: GameContext


    /**
     * just for optimization, only for it.
     * buffer that using like the container for get and hold hitbox center of newCard in:
     * applyCardToNotEmptyStack(...)
     */
    private val newCardCenter = Vector2(-1f, -1f)

    override fun update(deltaTime: Float) {
        if (gameContext.dndSelectedEntity != null
                && gameContext.dndEntityStatus == DROPPED) {
            processEntity(gameContext.dndSelectedEntity!!, deltaTime)
            gameContext.dndEntityStatus = NONE
            gameContext.dndSelectedEntity = null
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        var cardApplyIntoStack = false
//        val entityTransComp = entity.findRequiredComponent(TransformComponent.mapper)
        for (stack in gameContext.stacks) {
            val stackComp = stack.findRequiredComponent(GameStacksComponent.mapper)

            cardApplyIntoStack = when (stackComp.isEmpty()) {
                true -> applyCardToEmptyStack(entity, stack, stackComp)
                false -> applyCardToNotEmptyStack(entity, stackComp)
            }
            if (cardApplyIntoStack) return

//            val lastCard = stackComp.getLastCard()
//            val stackLastCardTransComp = lastCard.findRequiredComponent(TransformComponent.mapper)

//            if (stackLastCardTransComp.shape.contains(entityTransComp.shape.getCenter(coordinateContainer))) {
//                val entityGameCardComp = entity.findRequiredComponent(GameCardComponent.mapper)
//                val stackLastGameCardComp = lastCard.findRequiredComponent(GameCardComponent.mapper)
//
//                if (!stackLastGameCardComp.setNextPredicate.evaluate(entityGameCardComp)) return
//                deleteGameCardFromStack(entity)
//                stackLastGameCardComp.next = entity
//                stackComp.addGameCard(entity)
//
//                val newPos = Vector2(stackLastCardTransComp.interpolatedPosition.x,
//                        stackLastCardTransComp.interpolatedPosition.y - CARD_STACK_OFFSET,
//                )
//                entityTransComp.setTotalPosition(newPos)
//                entityGameCardComp.moveNextCards(newPos)
//
//                returnCardBack = false
//
////                gameContext.dndSelectedEntity = null
////                gameContext.dndEntityStatus = BINDING_SUCCESS
//                // TODO - see AttachSystem in DarkMatter
//            }
        }
        if (!cardApplyIntoStack) {
            returnGameCardToStack(entity)
//                gameContext.dndEntityStatus = BINDING_FAIL
        }
    }

    private fun applyCardToEmptyStack(entity: Entity, stack: Entity, stackStackComp: GameStacksComponent): Boolean {
        println("EMPTY")
        val entityTransComp = entity.findRequiredComponent(TransformComponent.mapper)
        val stackTransComp = stack.findRequiredComponent(TransformComponent.mapper)

//        if (newCard != stackLastCard) return false

        if (stackTransComp.shape.contains(entityTransComp.shape.getCenter(newCardCenter))) {
            val entityGameCardComp = entity.findRequiredComponent(GameCardComponent.mapper)

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

    private fun applyCardToNotEmptyStack(entity: Entity, stackComp: GameStacksComponent): Boolean {
        println("NOT EMPTY")
        val newCard = GameCardAdapter(entity)
        val stackLastCard = GameCardAdapter(stackComp.getLastCard())
        newCard.transfComp.shape.getCenter(newCardCenter)

        println("DEB")
        println("newCard =       ${newCard.hashCode()}")
        println("stackLastCard = ${stackLastCard.hashCode()}")
        println("DEB")
        if (newCard == stackLastCard) return false

        if (stackLastCard.transfComp.shape.contains(newCardCenter)) {

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
//    private fun applyCardToNotEmptyStack(entity: Entity, stackComp: GameStacksComponent): Boolean {
//        val entityTransComp = entity.findRequiredComponent(TransformComponent.mapper)
//        val lastCard = stackComp.getLastCard()
//        val stackLastCardTransComp = lastCard.findRequiredComponent(TransformComponent.mapper)
//
//        if (stackLastCardTransComp.shape.contains(entityTransComp.shape.getCenter(coordinateContainer))) {
//            val entityGameCardComp = entity.findRequiredComponent(GameCardComponent.mapper)
//            val stackLastGameCardComp = lastCard.findRequiredComponent(GameCardComponent.mapper)
//
//            if (!stackLastGameCardComp.setNextPredicate.test(entityGameCardComp)) return false
//            deleteGameCardFromStack(entity)
//            stackLastGameCardComp.next = entity
//            stackComp.addGameCard(entity)
//
//            val newPos = Vector2(
//                    stackLastCardTransComp.interpolatedPosition.x,
//                    stackLastCardTransComp.interpolatedPosition.y - CARD_STACK_OFFSET,
//            )
//            entityTransComp.setTotalPosition(newPos)
//            entityGameCardComp.moveNextCards(newPos)
//            return true
//        } else return false
//    }

    /**
     * Unbinding card from it's stack.
     */
    private fun unbindCardFromStack(card: Entity) {
        for (stack in gameContext.stacks) {
            val stackComp = stack.findRequiredComponent(GameStacksComponent.mapper)
            if (stackComp.contains(card)) {
                stackComp.removeGameCard(card)
                return
            }
        }
    }

    private fun returnGameCardToStack(card: Entity) {
        for (stack in gameContext.stacks) {
            val stackComp = stack.findRequiredComponent(GameStacksComponent.mapper)
            if (stackComp.contains(card)) {
                val entityTransComp = card.findRequiredComponent(TransformComponent.mapper)
                val entityGameCardComp = card.findRequiredComponent(GameCardComponent.mapper)
                val stackLastCardTransComp = stackComp.getLastCard().findRequiredComponent(TransformComponent.mapper)
                val newPos = Vector2(
                        stackLastCardTransComp.interpolatedPosition.x,
                        stackLastCardTransComp.interpolatedPosition.y - CARD_STACK_OFFSET,
                )
                entityTransComp.setTotalPosition(newPos)
                entityGameCardComp.moveNextCards(newPos)
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
