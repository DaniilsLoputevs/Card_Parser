package cardparser.ashley.systems

import cardparser.ashley.components.TransformComponent
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import cardparser.ashley.components.GameStackComponent
import cardparser.ashley.components.MainStackComponent
import cardparser.ashley.components.FoundationDragComponent
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import ktx.ashley.exclude

class StandardDragCardSystem(val gameEventManager: GameEventManager) : IteratingSystem(
    allOf(TransformComponent::class, GameStackComponent::class)
        .exclude(MainStackComponent::class, FoundationDragComponent::class).get()
), GameEventListener {

    private val storage: MutableList<GameCardAdapter> = mutableListOf()
    private var shiftRange = 30L
    private var startSearch = 0
    private var searchCard: GameCardAdapter? = null
    private var cursorPosition: Vector2 = Vector2().setZero()
    private var memorizeStack: GameStackAdapter? = null
    private var z = 200F
    private var step = 0F

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        gameEventManager.addListener(GameEvent.StartDragEvent::class, this)
        gameEventManager.addListener(GameEvent.DragEvent::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        gameEventManager.removeListener(GameEvent.StartDragEvent::class, this)
        gameEventManager.removeListener(GameEvent.DragEvent::class, this)
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        startSearch = 0
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val stack = GameStackAdapter(entity)
        when {
            startSearch == 1 && storage.size == 0 && stack.containsPosInTotalHitBox(cursorPosition) -> {
                searchCard = stack.gameStackComp.findCardByPos(cursorPosition)
                searchCard?.let {
                    stack.gameStackComp.transferCardsToList(it, storage)
                    memorizeStack = stack
                }
            }
            startSearch > 1 && storage.size > 0 -> {
                z = 200F
                step = 0F
                storage.forEach {
                    it.transComp.run {
                        setDepth(z)
                        setPosition(cursorPosition.apply { y += step })
                    }
                    step -= shiftRange
                    z += 1
                }
            }
            startSearch == 0 && storage.size > 0 -> {
                gameEventManager.dispatchEvent(GameEvent.DropEvent.apply {
                    previousStack = memorizeStack
                    cardList.addAll(storage)
                    position = cursorPosition
                    storage.clear()
                })
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
}
