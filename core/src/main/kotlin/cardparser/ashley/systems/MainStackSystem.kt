package cardparser.ashley.systems

import cardparser.CARD_WIDTH
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.ashley.components.klondike.MainStackComponent
import cardparser.ashley.systems.parts.screeninput.CardMoveProcessor.TouchStatus.TOUCH
import cardparser.gameStrucures.GameContext
import cardparser.gameStrucures.GameRepository
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get

class MainStackSystem : SortedIteratingSystem(
        allOf(TransformComponent::class, MainStackComponent::class).get(),
        compareBy { entity -> entity[MainStackComponent.mapper] }) {

    private val logger = ktx.log.logger<MainStackSystem>()

    private val secondStackPos = Vector2(60.25f * 2 + CARD_WIDTH, 520f)

    private var transferCard: GameCardAdapter? = null
    private var currPosition: Vector2 = Vector2(Vector2.Zero)
    private lateinit var currentsStack: GameStackAdapter

    lateinit var gameViewport: Viewport
    lateinit var context: GameContext
    lateinit var gameRep: GameRepository


    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (context.touchListStatus == TOUCH) {
            calculateCursorPosition()
            val mainStackComp = entity[MainStackComponent.mapper]!!

//            currentsStack.entity = entity
            when (mainStackComp.order) {
                0 -> logicIfClosedStuck(entity, mainStackComp)
                1 -> logicIfOpenStuck(entity, mainStackComp)
            }
        }
    }

    private fun calculateCursorPosition() {
        currPosition.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        gameViewport.unproject(currPosition)
    }

    private fun logicIfClosedStuck(entity: Entity, mainStackComponent: MainStackComponent) {
        if (currentsStack.containsPos(currPosition) && currentsStack.getCards().isNotEmpty()) {
            transferCard = currentsStack.getCards().removeLast()
        }
    }

    private fun logicIfOpenStuck(entity: Entity, mainStackComponent: MainStackComponent) {
        transferCard?.let {
            currentsStack.getCards().add(it)
            it.transComp.setPosition(secondStackPos)
//            context.touchList.removeAt(context.touchList.indexOf(transferCard))
//            eventManager.dispatchEvent(GameEvent.BindingCards.apply {
//                this.cards.addAll(touchList)
//            })
            transferCard = null
        }

    }
}
