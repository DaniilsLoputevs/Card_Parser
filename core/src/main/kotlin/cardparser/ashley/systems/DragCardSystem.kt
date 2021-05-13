package cardparser.ashley.systems

import cardparser.CARD_STACK_OFFSET
import cardparser.ashley.components.DragComp
import cardparser.ashley.components.TransformComp
import cardparser.ashley.entities.Card
import cardparser.ashley.entities.Stack
import cardparser.event.GameEvent
import cardparser.event.GameEventListener
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf

/**
 * TODO - Почему длять, система которая работает со стеками, называется ...CardSystem?????????
 */
class DragCardSystem(val gameEventManager: GameEventManager) : IteratingSystem(
        allOf(TransformComp::class, DragComp::class).get()
), GameEventListener {
    private val capturedCards: MutableList<Card> = mutableListOf()
    private val memorizeStack = Stack()
    private val eachStack = Stack()
    private val cursorPosition: Vector2 = Vector2().setZero()
    private val captureOffset: Vector2 = Vector2().setZero()

    private var startSearch = 0


    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        startSearch = 0
    }

    private fun takeCardsFromStack() = (startSearch == 1) && capturedCards.isEmpty() && eachStack.containsPosInTotalHitBox(cursorPosition)
    private fun dragTakenCards() = (startSearch > 1) && (capturedCards.size > 0)
    private fun dropTakenCards() = (startSearch == 0) && (capturedCards.size > 0)

    /** process each stack */
    override fun processEntity(entity: Entity, deltaTime: Float) {
        eachStack.entity = entity
        when {
            this.takeCardsFromStack() -> {
                eachStack[cursorPosition]?.let {
//                    logger.dev("on Touch")
//                    logger.dev("touch - cursor") { cursorPosition.toString() }

                    eachStack.transferCardsToList(it, capturedCards)
                    memorizeStack.entity = eachStack.entity
                    refreshCaptureOffset(it)

//                    logger.dev("touch - cursor") { captureOffset.toString() }
                }
            }
            this.dragTakenCards() -> {
//                logger.dev("cursor", cursorPosition)
//                logger.dev("Drag - first card pos") { capturedCards[0].transComp.position.toString() }
                dragTouchList()
            }
            this.dropTakenCards() -> {
//                logger.dev("on Drop")
                gameEventManager.dispatchEvent(GameEvent.DropEvent.apply {
                    previousStack.entity = memorizeStack.entity
                    cardList.addAll(capturedCards)
                    position.set(cursorPosition)
                    capturedCards.clear()
                })
            }
        }
    }

    override fun onEvent(event: GameEvent) {
        if (event is GameEvent.DragEvent) {
            startSearch += 2
            cursorPosition.set(event.cursor)
        }
        if (event is GameEvent.StartDragEvent) {
            startSearch += 1
            cursorPosition.set(event.cursor)
        }
    }

    /** Refresh coordinates of all cards that is in dragged stack if we shift cursor and drag stack. */
    private fun dragTouchList() {
        val posX = cursorPosition.x - captureOffset.x
        var posY = cursorPosition.y - captureOffset.y
        capturedCards.forEach {
            it.setPos(posX, posY, it.pos().z * 1000f)
            posY -= CARD_STACK_OFFSET
        }
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
        gameEventManager.addListener(GameEvent.StartDragEvent::class, this)
        gameEventManager.addListener(GameEvent.DragEvent::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        gameEventManager.removeListener(GameEvent.StartDragEvent::class, this)
        gameEventManager.removeListener(GameEvent.DragEvent::class, this)
    }

    companion object {
        private val logger = loggerApp<DragCardSystem>()
    }
}
