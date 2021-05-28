package cardparser.ashley.systems.input.screen

import cardparser.STACK_OPEN_CARD_OFFSET
import cardparser.ashley.systems.input.screen.TouchInputStatus.*
import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import cardparser.tasks.StackBinding
import cardparser.tasks.TaskManager
import com.badlogic.gdx.math.Vector2

class CardDragListenerNew(
        private val botUpStacks: MutableList<Stack>,
        override val cursor: CursorInfo
) : TouchInputListener {
    private val capturedCards: MutableList<Card> = mutableListOf()
    private val memorizeStack = Stack()
    private val captureOffset: Vector2 = Vector2().setZero()

    override fun listeningActions(): Set<TouchInputStatus> {
//        Debug.act("F", "CardDragListener") { logger.debug("capturedCards", capturedCards) }
        return setOf(GRAB, DRAG, DROP)
    }

    override fun onAction() {
        logger.dev("on action", cursor.status)
        when (cursor.status) {
            GRAB -> captureCards()
            DRAG -> dragTouchList()
            DROP -> run { if (capturedCards.isNotEmpty()) commitCapturedCardsBinding(); capturedCards.clear() }
        }
//        logger.dev("capturedCards", capturedCards)
    }

    private fun captureCards() {
        if (capturedCards.isNotEmpty()) capturedCards.clear()
        botUpStacks.find { it.containsInArea(cursor.pos) }?.let {
            logger.dev("FOUND")
            logger.dev("current stack", it)
            it[cursor.pos]?.let { card ->
                it.transferCardsToList(card, capturedCards)
                memorizeStack.entity = it.entity
                setCaptureCardOffset(card)
            }
        }

//        logger.dev("onStartDrag :: state")
//        logger.dev("capturedCards", capturedCards)
//        logger.dev("cursorPosition", cursorPosition)

//        if (memorizeStack.entity.components.size() > 0) {
//            logger.dev("memorizeStack", memorizeStack)
//        } else {
//            logger.error("memorizeStack is NONE")
//        }
    }


    private fun dragTouchList() {
        if (capturedCards.isEmpty()) return
        val posX = cursor.pos.x - captureOffset.x
        var posY = cursor.pos.y - captureOffset.y
        capturedCards.forEach {
            it.setPos(posX, posY, it.pos().z * 1000f)
            posY -= STACK_OPEN_CARD_OFFSET
        }
    }

    private fun commitCapturedCardsBinding() {
        logger.debug("commit StackBinding")
        TaskManager.commit(StackBinding(TaskManager.genId(), memorizeStack, capturedCards.toMutableList(), cursor.pos.cpy()))
    }

    /** Calculate a card position with relating to the cursor if we start dragged the card. */
    private fun setCaptureCardOffset(card: Card) {
        captureOffset.set(
                cursor.pos.x - card.pos().x,
                cursor.pos.y - card.pos().y
        )
    }

    companion object {
        private val logger by lazy { loggerApp<CardDragListenerNew>() }
    }

}