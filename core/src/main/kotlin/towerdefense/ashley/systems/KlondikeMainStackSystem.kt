package towerdefense.ashley.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.info
import towerdefense.ashley.components.KlondikeGame.*
import towerdefense.ashley.components.TransformComponent
import towerdefense.gameStrucures.GameContext
import towerdefense.gameStrucures.adapters.*
import towerdefense.ashley.systems.parts.screeninput.CardMoveProcessor.TouchStatus.*

class KlondikeMainStackSystem : SortedIteratingSystem(
    allOf(TransformComponent::class, KlondikeMainStackComponent::class).get(),
    compareBy { entity -> entity[KlondikeMainStackComponent.mapper] }) {

    private val logger = ktx.log.logger<KlondikeMainStackSystem>()

    private var transferCard: GameCardAdapter? = null
    private var currPosition: Vector2 = Vector2(Vector2.Zero)
    private lateinit var currentsStack: GameStackAdapter
    lateinit var gameViewport: Viewport
    lateinit var context: GameContext

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (context.touchListStatus == TOUCH) {
            calculateCursorPosition()
            val mainComp: KlondikeMainStackComponent = entity[KlondikeMainStackComponent.mapper]!!
            if (mainComp.order == 0) {
                logicIfClosedStuck(entity, mainComp)
            } else if (mainComp.order == 1) {
                logicIfOpenStuck(entity, mainComp)
            }
        }
    }

    private fun calculateCursorPosition() {
        currPosition.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        gameViewport.unproject(currPosition)
    }

    private fun logicIfClosedStuck(entity: Entity, mainStackComponent: KlondikeMainStackComponent) {
        currentsStack = GameStackAdapter(entity)
        if (currentsStack.containsPos(currPosition)
            && currentsStack.getCards().isNotEmpty()
        ) {
            logger.info { "before stack status = ${currentsStack.toString()}" }
            transferCard = currentsStack.getCards().removeLast()
            logger.info { "before stack status after removeLast = ${currentsStack.toString()}" }
        }
    }

    private fun logicIfOpenStuck(entity: Entity, mainStackComponent: KlondikeMainStackComponent) {
        currentsStack = GameStackAdapter(entity)
        transferCard?.let {
            logger.info { "after stack status = ${currentsStack.toString()}" }
            currentsStack.getCards().add(it)
            logger.info { "after stack status after add = ${currentsStack.toString()}" }
            transferCard = null;
        }

    }
}
