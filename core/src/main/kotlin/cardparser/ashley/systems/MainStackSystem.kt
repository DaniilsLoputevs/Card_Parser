package cardparser.ashley.systems

import cardparser.ashley.components.MainStackComp
import cardparser.ashley.components.StackComp
import cardparser.ashley.components.TransformComp
import cardparser.entities.Card
import cardparser.entities.MainStack
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

class MainStackSystem : SortedIteratingSystem(
        allOf(TransformComp::class, StackComp::class, MainStackComp::class).get(),
        compareBy { entity -> entity[MainStackComp.mapper] }
), GameEventListener {

//class MainStackSystem(val gameEventManager: GameEventManager)
//    : SortedIteratingSystem(MAIN_STACK_FAMILY, compareBy { entity -> entity[MainStackComp.mapper] }),
//        GameEventListener {

    private val currStack = MainStack()
    private val transferCard: MutableList<Card> = mutableListOf()
    private val pos: Vector2 = Vector2(-1f, -1f)

    private var isPosActual = false
    private var returnAll = false
    private var offSystem = 0

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        offSystem = 0
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (isPosNotDefault()) {
            currStack.entity = entity
            offSystem += currStack.size()
            when (currStack.order()) {
                0 -> {
                    when {
                        currStack.isInShape(pos) && currStack.isEmpty() && transferCard.size == 0 -> {
                            returnAll = true
                        }
                        transferCard.size > 0 -> {
                            transferCard.forEach { it.open(false) }
                            currStack.addAll(transferCard)
                            transferCard.clear()
                            resetPosBuffer()
                        }
                        currStack.isInShape(pos) && transferCard.size == 0 -> {
                            transferCard.add(currStack.removeAt(currStack.size() - 1))
                        }
                        else -> resetPosBuffer()
                    }
                }
                1 -> {
                    when {
                        returnAll -> {
                            returnAll = false
                            currStack.cards().asReversed().forEach { transferCard.add(it) }
                            currStack.clear()
                            offSystem += currStack.size() + transferCard.size
                            if (offSystem == 0) {
                                logger.info("MainStackSystem is empty. System is stopped")
                                setProcessing(false)
                            }
                        }
                        transferCard.size > 0 -> {
                            transferCard.forEach { it.open(true); it.touchable(true); it.pos().z += 50 }
                            currStack.addAll(transferCard)
                            transferCard.clear()
                            resetPosBuffer()
                        }
                        else -> resetPosBuffer()
                    }
                }
            }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.TouchEvent) {
            pos.set(event.position)
            isPosActual = true
        }
    }

    private fun isPosNotDefault() = (pos.x > -1f) && (pos.y > -1f)
    private fun resetPosBuffer() = run { pos.set(-1f, -1f); isPosActual = false }

    override fun addedToEngine(engine: Engine?) {
        GameEventManager.addListener(GameEvent.TouchEvent::class, this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        GameEventManager.removeListener(GameEvent.TouchEvent::class, this)
        super.removedFromEngine(engine)
    }

    companion object {
        private val logger = loggerApp<MainStackSystem>()
    }
}
