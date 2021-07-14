package cardparser.event

import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.GdxSet
import kotlin.reflect.KClass

private const val INITIAL_LISTENER_CAPACITY = 8

sealed class GameEvent {

    object StartGame : GameEvent() {
        override fun toString() = "StartGame"
    }

    object StopGame : GameEvent() {
        override fun toString() = "StopGame"
    }

    object WinGame : GameEvent() {
        override fun toString() = "StopGame"
    }

    object TouchEvent : GameEvent() {
        var position: Vector2 = Vector2().setZero()

        override fun toString(): String {
            return "TouchEvent -- position = $position"
        }
    }

    object StartDragEvent : GameEvent() {
        var cursor: Vector2 = Vector2().setZero()
        var memorized: Vector2 = Vector2().setZero()

        override fun toString(): String {
            return "StartDragEvent -- cursor = $cursor & memorized = $memorized"
        }
    }

    object DragEvent : GameEvent() {
        var cursor: Vector2 = Vector2().setZero()
        var memorized: Vector2 = Vector2().setZero()

        override fun toString(): String {
            return "DragEvent -- cursor = $cursor & memorized = $memorized"
        }
    }

    object DropEvent : GameEvent() {
        //        var activityID: Int = 8
        var prevStack: Stack = Stack()
        var cardList: MutableList<Card> = mutableListOf()
        var position: Vector2 = Vector2().setZero()

        override fun toString(): String {
            return "DropEvent -- prevStack = $prevStack & cardList = $cardList & position = $position"
        }
    }
}

interface GameEventListener {
    fun onEvent(event: GameEvent)
}

object GameEventManager {
    private val listeners = ObjectMap<KClass<out GameEvent>, GdxSet<GameEventListener>>()
    private val eventHistory = mutableListOf<GameEvent>()

    fun addListener(type: KClass<out GameEvent>, listener: GameEventListener) {
        var eventListeners = listeners[type]
        if (eventListeners == null) {
            eventListeners = GdxSet()
            listeners.put(type, eventListeners)
        }
        eventListeners.add(listener)
    }

    fun removeListener(type: KClass<out GameEvent>, listener: GameEventListener) =
            listeners[type]?.remove(listener)

    fun removeListener(listener: GameEventListener) {
        listeners.values().forEach { it.remove((listener)) }
    }

    fun dispatchEvent(event: GameEvent) {
        if (event !is GameEvent.DragEvent) eventHistory.add(event)
        listeners[event::class]?.let { it.forEach { each -> each.onEvent(event) } }
                ?: logger.warm("Event = ${event::class.simpleName}, doesn't have listeners!")
    }

    fun logHistory() = logger.error("event history", eventHistory)
}

private val logger = loggerApp<GameEventManager>()
