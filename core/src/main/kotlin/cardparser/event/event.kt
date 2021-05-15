package cardparser.event

import cardparser.ashley.entities.Card
import cardparser.ashley.entities.Stack
import cardparser.logger.loggerApp
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.GdxSet
import kotlin.reflect.KClass

private const val INITIAL_LISTENER_CAPACITY = 8

sealed class GameEvent {

    object NoneEvent : GameEvent()

    object StartGame : GameEvent()
    object StopGame : GameEvent()

    object TouchEvent : GameEvent() {
        var position: Vector2 = Vector2().setZero()
    }

    object StartDragEvent : GameEvent() {
        var cursor: Vector2 = Vector2().setZero()
        var memorized: Vector2 = Vector2().setZero()
    }

    object DragEvent : GameEvent() {
        var cursor: Vector2 = Vector2().setZero()
        var memorized: Vector2 = Vector2().setZero()
    }

    object DropEvent : GameEvent() {
        var previousStack: Stack = Stack()
        var cardReturn: Boolean = false
        var cardList: MutableList<Card> = mutableListOf()
        var position: Vector2 = Vector2().setZero()
    }
}

interface GameEventListener {
    fun onEvent(event: GameEvent)
}

class GameEventManager {
    private val listeners = ObjectMap<KClass<out GameEvent>, GdxSet<GameEventListener>>()

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
        listeners[event::class]?.let { it.forEach { each -> each.onEvent(event) } }
//                ?: logger.warm("Event = ${event::class.simpleName}, doesn't have listeners!")
    }
}

private val logger = loggerApp<GameEventManager>()