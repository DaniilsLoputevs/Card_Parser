package cardparser.event

import cardparser.ashley.components.adapters.GameCardAdapter
import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.GdxSet
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import kotlin.reflect.KClass

private val LOG = logger<GameEventManager>()
private const val INITIAL_LISTENER_CAPACITY = 8

sealed class GameEvent {

    object BindingCards : GameEvent() {
        val cards = mutableListOf<GameCardAdapter>()
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
            eventListeners = GdxSet(INITIAL_LISTENER_CAPACITY)
            listeners.put(type, eventListeners)
        }

        if (eventListeners.add(listener)) {
//            LOG.debug { "Adding listener of type $type: $listener" }
        } else {
            LOG.error { "Trying to add already existing listener of type $type: $listener" }
        }
    }

    fun removeListener(type: KClass<out GameEvent>, listener: GameEventListener) {
        val eventListeners = listeners[type]
        when {
            eventListeners == null -> {
                LOG.error { "Trying to remove listener $listener from non-existing listeners of type $type" }
            }
            listener !in eventListeners -> {
                LOG.error { "Trying to remove non-existing listener of type $type: $listener" }
            }
            else -> {
//                LOG.debug { "Removing listener of type $type: $listener" }
                eventListeners.remove(listener)
            }
        }
    }

    /**
     * This function removes the [listener] from all [types][GameEvent]. It is
     * slightly more efficient to use [removeListener] if you know the exact type(s).
     */
    fun removeListener(listener: GameEventListener) {
        LOG.debug { "Removing $listener from all types" }
        listeners.forEach { it.value.remove(listener) }
    }

    fun dispatchEvent(event: GameEvent) {
//        LOG.debug { "Dispatch event $event" }
        listeners[event::class]?.forEach { it.onEvent(event) }
    }
}
