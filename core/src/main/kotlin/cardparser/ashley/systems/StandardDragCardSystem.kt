package cardparser.ashley.systems

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

class StandardDragCardSystem(val gameEventManager: GameEventManager) : IteratingSystem(
    allOf(TransformComponent::class, StandardDragComponent::class).get()
), GameEventListener {

    private val storeList: MutableList<GameCardAdapter> = mutableListOf()
    private var stack: GameStackAdapter = GameStackAdapter()
    private var shiftRange = 30L
    private var startSearch = 0
    private var cursorPosition: Vector2 = Vector2().setZero()
    private var memorizeStack: GameStackAdapter = GameStackAdapter()
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
        stack.entity = entity
        when {
            startSearch == 1 && storeList.size == 0 && stack.containsPosInTotalHitBox(cursorPosition) -> {
                stack.gameStackComp
                    .findCardByPos(cursorPosition)?.let {
                    stack.gameStackComp.transferCardsToList(it, storeList)
                    memorizeStack.entity = stack.entity
                }
            }
            startSearch > 1 && storeList.size > 0 -> {
                z = 200F
                step = 0F
                storeList.forEach {
                    it.transComp.run {
                        setDepth(z)
                        setPosition(cursorPosition.apply { y += step })
                    }
                    step -= shiftRange
                    z += 1
                }
            }
            startSearch == 0 && storeList.size > 0 -> {
                gameEventManager.dispatchEvent(GameEvent.DropEvent.apply {
                    previousStack.entity = memorizeStack.entity
                    cardList.addAll(storeList)
                    position = cursorPosition
                    storeList.clear()
                })
            }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DragEvent) {
            startSearch += 2
            cursorPosition = event.cursor
        }
        if (event is GameEvent.StartDragEvent) {
            startSearch += 1
            cursorPosition = event.cursor
        }
    }
}
