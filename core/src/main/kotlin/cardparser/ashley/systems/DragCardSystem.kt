package cardparser.ashley.systems

import cardparser.STACK_OPEN_CARD_OFFSET
import cardparser.ashley.components.DragComp
import cardparser.ashley.components.TransformComp
import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import cardparser.tasks.StackBinding
import cardparser.tasks.TaskManager
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf

@Deprecated("off")
class DragCardSystem : IteratingSystem(
        allOf(TransformComp::class, DragComp::class).get()
), GameEventListener {
    private val capturedCards: MutableList<Card> = mutableListOf()
    private val stack = Stack()
    private val memorizeStack = Stack()
    private val cursorPosition: Vector2 = Vector2().setZero()
    private val captureOffset: Vector2 = Vector2().setZero()

    override fun update(deltaTime: Float) = TODO("Not yet implemented")
    override fun processEntity(entity: Entity?, deltaTime: Float) = TODO("Not yet implemented")

    /* Real part */

    private fun isStartDrag(event: GameEvent) = event is GameEvent.StartDragEvent && capturedCards.isEmpty()
    private fun isDrag(event: GameEvent) = event is GameEvent.DragEvent && capturedCards.isNotEmpty()
    private fun isDrop(event: GameEvent) = event is GameEvent.DropEventNew && capturedCards.isNotEmpty()

    override fun onEvent(event: GameEvent) {
        setCursorPos(event)
        when {
            isStartDrag(event) -> onStartDrag()
            isDrag(event) -> dragTouchList()
            isDrop(event) -> commitCapturedCardsBinding()
        }
    }

    private fun setCursorPos(event: GameEvent) {
        if (event is GameEvent.StartDragEvent) cursorPosition.set(event.cursor)
        if (event is GameEvent.DragEvent) cursorPosition.set(event.cursor)
        if (event is GameEvent.DropEventNew) cursorPosition.set(event.cursor)
    }


    private fun onStartDrag() {
        logger.dev("onStartDrag")
        findStackByPos(cursorPosition)?.let {
            logger.dev("FOUND")
            it[cursorPosition]?.let { card ->
                stack.transferCardsToList(card, capturedCards)
                memorizeStack.entity = stack.entity
                refreshCaptureOffset(card)
            }
        }

//        logger.dev("onStartDrag :: state")
//        logger.dev("capturedCards", capturedCards)
//        logger.dev("cursorPosition", cursorPosition)
//        logger.dev("memorizeStack", memorizeStack)
    }

    private fun findStackByPos(pos: Vector2): Stack? {
        entities.forEach {
            stack.entity = it
            if (stack.containsInArea(pos)) return stack
        }
        return null
    }

    /** Refresh coordinates of all cards that is in dragged stack if we shift cursor and drag stack. */
    private fun dragTouchList() {
        val posX = cursorPosition.x - captureOffset.x
        var posY = cursorPosition.y - captureOffset.y
        capturedCards.forEach {
            it.setPos(posX, posY, it.pos().z * 1000f)
            posY -= STACK_OPEN_CARD_OFFSET
        }
    }

    private fun commitCapturedCardsBinding() {
        logger.error("Commit from Deprecated code!!!!!!")
        TaskManager.commit(StackBinding(TaskManager.genId(), memorizeStack, capturedCards, cursorPosition))
    }

    /** Calculate a card position with relating to the cursor if we start dragged the card. */
    private fun refreshCaptureOffset(card: Card) {
        captureOffset.set(
                cursorPosition.x - card.pos().x,
                cursorPosition.y - card.pos().y
        )
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        GameEventManager.addListener(GameEvent.StartDragEvent::class, this)
        GameEventManager.addListener(GameEvent.DragEvent::class, this)
        GameEventManager.addListener(GameEvent.DropEventNew::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        GameEventManager.removeListener(GameEvent.StartDragEvent::class, this)
        GameEventManager.removeListener(GameEvent.DragEvent::class, this)
        GameEventManager.removeListener(GameEvent.DropEventNew::class, this)
    }

    companion object {
        private val logger = loggerApp<DragCardSystem>()
    }
}
