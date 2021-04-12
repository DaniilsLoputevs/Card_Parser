package towerdefense.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import towerdefense.CARD_WIDTH
import towerdefense.ashley.components.klondike.*
import towerdefense.ashley.components.TransformComponent
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.*
import towerdefense.ashley.systems.parts.screeninput.CardMoveProcessor.TouchStatus.*

class MainStackSystem : SortedIteratingSystem(
    allOf(TransformComponent::class, MainStackComponent::class).get(),
    compareBy { entity -> entity[MainStackComponent.mapper] }) {

    private val logger = ktx.log.logger<MainStackSystem>()

    private var transferCard: GameCardAdapter? = null
    private var currPosition: Vector2 = Vector2(Vector2.Zero)
    private lateinit var currentsStack: GameStackAdapter
    private val buffer = Vector2(
        60.25f * 2 + CARD_WIDTH,
        520f)
    lateinit var gameViewport: Viewport
    lateinit var context: GameContext

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (context.touchListStatus == TOUCH) {
            calculateCursorPosition()
            val mainStackComp = entity[MainStackComponent.mapper]!!

            when(mainStackComp.order) {
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
        currentsStack = GameStackAdapter(entity)
        if (currentsStack.containsPos(currPosition)
            && currentsStack.getCards().isNotEmpty()
        ) {
            transferCard = currentsStack.getCards().removeLast()
        }
    }

    private fun logicIfOpenStuck(entity: Entity, mainStackComponent: MainStackComponent) {
        currentsStack = GameStackAdapter(entity)
        transferCard?.let {
            currentsStack.getCards().add(it)
            it.transComp.setPosition(buffer)
            context.touchList.removeAt(context.touchList.indexOf(transferCard))
            transferCard = null
        }

    }
}
