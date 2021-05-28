package cardparser.common

import cardparser.logger.loggerApp

class DeltaTimerDown(
        var timer: Float = 0f, // game sec - not OS sec
        var currentAccumulate: Float = timer, // game sec - not OS sec
        var isActive: Boolean = true
) {

    fun update(deltaTime: Float): Boolean {
        if (!isActive) return false
        currentAccumulate -= deltaTime
        return if (currentAccumulate <= 0) {
            isActive = false
            reset()
            false
        } else true
    }

    fun reset() = kotlin.run { currentAccumulate = 0f }
    fun resetAll() = kotlin.run { timer = 0f; currentAccumulate = 0f }

    companion object {
        private val logger by lazy { loggerApp<DeltaTimerDown>() }
    }
}