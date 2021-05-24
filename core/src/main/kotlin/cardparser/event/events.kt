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
        var eventId: Int = GameEventManager.genId()
        override fun toString() = "$eventId || StartGame"
    }

    object StopGame : GameEvent() {
        var eventId: Int = GameEventManager.genId()
        override fun toString() = "$eventId || StopGame"
    }

    object TouchEvent : GameEvent() {
        var eventId: Int = GameEventManager.genId()
        var position: Vector2 = Vector2().setZero()

        override fun toString(): String {
            return "$eventId || TouchEvent -- position = $position"
        }
    }

    object StartDragEvent : GameEvent() {
        var eventId: Int = GameEventManager.genId()
        var cursor: Vector2 = Vector2().setZero()
        var memorized: Vector2 = Vector2().setZero()

        override fun toString(): String {
            return "$eventId || StartDragEvent -- cursor = $cursor & memorized = $memorized"
        }
    }

    object DragEvent : GameEvent() {
        var eventId: Int = GameEventManager.genId()
        var cursor: Vector2 = Vector2().setZero()
        var memorized: Vector2 = Vector2().setZero()

        override fun toString(): String {
            return "$eventId || DragEvent -- cursor = $cursor & memorized = $memorized"
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

    object DropEventNew : GameEvent() {
        var eventId: Int = GameEventManager.genId()
        var cursor: Vector2 = Vector2()

        override fun toString(): String {
            return "$eventId || DropEvent -- position = $cursor"
        }
    }
}

interface GameEventListener {
    fun onEvent(event: GameEvent)
}

object GameEventManager {
    private var nextEventId: Int = 0
    private val listeners = ObjectMap<KClass<out GameEvent>, GdxSet<GameEventListener>>()
    private val eventHistory = mutableListOf<String>()

    fun genId() = nextEventId++

    fun addListener(eventTypes: List<KClass<out GameEvent>>, listener: GameEventListener) {
        eventTypes.forEach { addListener(it, listener) }
    }

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
        if (event !is GameEvent.DragEvent) eventHistory.add(event.toString())
        listeners[event::class]?.let { it.forEach { each -> each.onEvent(event) } }
                ?: logger.warm("Event = ${event::class.simpleName}, doesn't have listeners!")
    }

    fun logHistory() = logger.error("event history", eventHistory)

    private val logger by lazy { loggerApp<GameEventManager>() }
}