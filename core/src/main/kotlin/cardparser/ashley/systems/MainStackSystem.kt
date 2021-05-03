package cardparser.ashley.systems

import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.MainStackComponent
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import ktx.ashley.get

class MainStackSystem(val gameEventManager: GameEventManager) : SortedIteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class, MainStackComponent::class).get(),
    compareBy { entity -> entity[MainStackComponent.mapper] }), GameEventListener {

    private val logger = ktx.log.logger<MainStackSystem>()

    private var transferCard: MutableList<GameCardAdapter> = mutableListOf()
    private var pos: Vector2? = null
    private var returnAll = false;

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
            val mainComp = entity[MainStackComponent.mapper]
            require(mainComp != null) { "MainStackComponent is empty" }
            val stackComp = entity[GameStackComponent.mapper]
            require(stackComp != null) { "GameStackComponent is empty" }
            val transComp = entity[TransformComponent.mapper]
            require(transComp != null) { "TransformComponent is empty" }

            when (mainComp.order) {
                0 -> {
                    if (transComp.shape.contains(pos) && stackComp.isEmpty() && transferCard.size == 0) {
                        returnAll = true
                    } else if(transferCard.size > 0) {
                        transferCard.forEach { it.gameCardComp.isCardOpen = false }
                        stackComp.cardStack.addAll(transferCard)
                        transferCard.clear()
                        pos = null
                    } else if (transComp.shape.contains(pos) && transferCard.size == 0) {
                        transferCard.add(stackComp.cardStack.removeAt(stackComp.size() - 1))
                    }
                }
                1 -> {
                    if (returnAll) {
                        returnAll = false
                        stackComp.cardStack.asReversed().forEach { transferCard.add(it) }
                        stackComp.cardStack.clear()
                    } else if (transferCard.size > 0) {
                        transferCard.forEach { it.gameCardComp.isCardOpen = true }
                        stackComp.cardStack.addAll(transferCard)
                        transferCard.clear()
                        pos = null
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.TouchEvent) {
            pos = event.position
        }
    }
}
