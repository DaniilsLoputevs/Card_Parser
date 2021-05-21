package cardparser.event

import cardparser.STACK_OPEN_CARD_OFFSET
import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import cardparser.tasks.StackBinding
import cardparser.tasks.TaskManager
import com.badlogic.gdx.math.Vector2
import kotlin.reflect.KClass

class CardDragListener(private val botAndUpStacks: MutableList<Stack>) : GameEventListener {
    private val capturedCards: MutableList<Card> = mutableListOf()
    private val memorizeStack = Stack()
    private val cursorPosition: Vector2 = Vector2().setZero()
    private val captureOffset: Vector2 = Vector2().setZero()


    private fun isStartDrag(event: GameEvent) = event is GameEvent.StartDragEvent && capturedCards.isEmpty()
    private fun isDrag(event: GameEvent) = event is GameEvent.DragEvent && capturedCards.isNotEmpty()
    private fun isDrop(event: GameEvent) = event is GameEvent.DropEventNew && capturedCards.isNotEmpty()

    override fun onEvent(event: GameEvent) {
        setCursorPos(event)
        when {
            isStartDrag(event) -> captureCards()
            isDrag(event) -> dragTouchList()
            isDrop(event) -> run { commitCapturedCardsBinding(); resetInnerState() }
        }
    }

    private fun resetInnerState() {
        capturedCards.clear()
    }

    private fun captureCards() {
        logger.dev("onStartDrag")
        botAndUpStacks.find { it.containsInArea(cursorPosition) }?.let {
            logger.dev("FOUND")
            logger.dev("current stack", it)
            it[cursorPosition]?.let { card ->
                it.transferCardsToList(card, capturedCards)
                memorizeStack.entity = it.entity
                refreshCaptureOffset(card)
            }
        }

//        logger.dev("onStartDrag :: state")
//        logger.dev("capturedCards", capturedCards)
//        logger.dev("cursorPosition", cursorPosition)

        if (memorizeStack.entity.components.size() > 0) {
            logger.dev("memorizeStack", memorizeStack)
        } else {
            logger.error("memorizeStack is NONE")
        }
    }


    private fun dragTouchList() {
        val posX = cursorPosition.x - captureOffset.x
        var posY = cursorPosition.y - captureOffset.y
        capturedCards.forEach {
            it.setPos(posX, posY, it.pos().z * 1000f)
            posY -= STACK_OPEN_CARD_OFFSET
        }
    }

    private fun commitCapturedCardsBinding() {
        logger.warm("commit Binding!!!!")
        TaskManager.commit(StackBinding(TaskManager.genId(), memorizeStack, capturedCards.toMutableList(), cursorPosition))
    }

    /** Calculate a card position with relating to the cursor if we start dragged the card. */
    private fun refreshCaptureOffset(card: Card) {
        captureOffset.set(
                cursorPosition.x - card.pos().x,
                cursorPosition.y - card.pos().y
        )
    }

    private fun setCursorPos(event: GameEvent) {
        if (event is GameEvent.StartDragEvent) cursorPosition.set(event.cursor)
        if (event is GameEvent.DragEvent) cursorPosition.set(event.cursor)
        if (event is GameEvent.DropEventNew) cursorPosition.set(event.cursor)
    }

    companion object {
        val eventTypes = listOf<KClass<out GameEvent>>(
                GameEvent.StartDragEvent::class,
                GameEvent.DragEvent::class,
                GameEvent.DropEventNew::class
        )
        private val logger by lazy { loggerApp<CardDragListener>() }
    }
}