package depricated

import cardparser.ashley.components.StandardDragComponent
import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf

@Deprecated("")
class FoundationDragSystem(val gameEventManager: GameEventManager) : IteratingSystem(
        allOf(TransformComponent::class, StandardDragComponent::class).get()
), GameEventListener {

    private val stack: GameStackAdapter = GameStackAdapter()
    private val storage: MutableList<GameCardAdapter> = mutableListOf()
    private val z = 200F
    var position: Vector2 = Vector2.Zero
    var startSearch = 0
    var cursorPosition: Vector2 = Vector2().setZero()
    var memorizeStack: GameStackAdapter = GameStackAdapter()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        startSearch = 0
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        stack.entity = entity
        when {
            startSearch == 1 && storage.size == 0 && stack.containsPosStack(cursorPosition) -> {
                if (stack.getCards().isNotEmpty()) {
                    storage.add(stack.getCards().removeAt(stack.getCards().size - 1))
                    memorizeStack = stack
                }
            }
            startSearch > 1 && storage.size > 0 -> {
                storage.forEach {
                    it.transComp.setX(cursorPosition.x)
                    it.transComp.setY(cursorPosition.y)
                    it.transComp.setDepth(z)
                }
            }
            startSearch == 0 && storage.size > 0 -> {
                gameEventManager.dispatchEvent(GameEvent.DropEvent.apply {
                    previousStack.entity = memorizeStack.entity
                    cardList.addAll(storage)
                    position = cursorPosition
                    storage.clear()
                })
            }
            else -> {
            }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DragEvent) {
            startSearch += 2
            cursorPosition.x = event.cursor.x
            cursorPosition.y = event.cursor.y
        }
        if (event is GameEvent.StartDragEvent) {
            startSearch += 1
            cursorPosition.x = event.cursor.x
            cursorPosition.y = event.cursor.y
        }
    }

    override fun addedToEngine(engine: Engine?) {
        gameEventManager.addListener(GameEvent.StartDragEvent::class, this)
        gameEventManager.addListener(GameEvent.DragEvent::class, this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameEventManager.removeListener(GameEvent.StartDragEvent::class, this)
        gameEventManager.removeListener(GameEvent.DragEvent::class, this)
        super.removedFromEngine(engine)
    }
}
