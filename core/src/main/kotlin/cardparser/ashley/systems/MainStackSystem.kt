package cardparser.ashley.systems

import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.MainStackComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.findComp
import cardparser.ashley.objects.Card
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import ktx.ashley.get

class MainStackSystem(val gameEventManager: GameEventManager) : SortedIteratingSystem(
        allOf(TransformComponent::class, GameStackComponent::class, MainStackComponent::class).get(),
        compareBy { entity -> entity[MainStackComponent.mapper] }
), GameEventListener {
    private var transferCard: MutableList<Card> = mutableListOf()
    private var pos: Vector2? = null
    private var returnAll = false

    override fun addedToEngine(engine: Engine?) {
        gameEventManager.addListener(GameEvent.TouchEvent::class, this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameEventManager.removeListener(GameEvent.TouchEvent::class, this)
        super.removedFromEngine(engine)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (pos != null) {
            val mainComp = entity.findComp(MainStackComponent.mapper)
            val stackComp = entity.findComp(GameStackComponent.mapper)
            val transComp = entity.findComp(TransformComponent.mapper)

            when (mainComp.order) {
                0 -> {
                    logger.dev("contains ${transferCard.size}")
                    if (transComp.shape.contains(pos) && stackComp.isEmpty() && transferCard.size == 0) {
                        logger.dev("transferCard stack")
                        returnAll = true
                    } else if (transferCard.size > 0) {
                        logger.dev("transferCard big")
                        transferCard.forEach { it.gameCardComp.isCardOpen = false }
                        stackComp.cardStack.addAll(transferCard)
                        transferCard.clear()
                        pos = null
                    } else if (transComp.shape.contains(pos) && transferCard.size == 0) {
                        logger.dev("start")
                        transferCard.add(stackComp.cardStack.removeAt(stackComp.size() - 1))
                    } else {
                        logger.dev("do null")
                        pos = null
                    }
                    logger.dev("after if")
                }
                1 -> {
                    when {
                        returnAll -> {
                            returnAll = false
                            stackComp.cardStack.asReversed().forEach { transferCard.add(it) }
                            stackComp.cardStack.clear()
                        }
                        transferCard.size > 0 -> {
                            transferCard.forEach { it.gameCardComp.isCardOpen = true }
                            transferCard.forEach { it.pos.z += 50 }
                            stackComp.cardStack.addAll(transferCard)
                            transferCard.clear()
                            pos = null
                        }
                        else -> {
                            pos = null
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.TouchEvent) {
            logger.dev("touch event")
            pos = event.position.cpy()
        }
    }

    companion object {
        private val logger = loggerApp<MainStackSystem>()
    }
}
