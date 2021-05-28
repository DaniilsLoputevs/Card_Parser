package cardparser.common

import cardparser.logger.loggerApp

/**
 * NOT TESTED!!!!
 */
class DeltaTimerUp(
        private var timer: Float = 0f,
        var currentAccumulate: Float = 0f,
        var isActive: Boolean = true
) {

    fun update(deltaTime: Float): Boolean {
        if (!isActive) return false
        currentAccumulate += deltaTime
        return if (currentAccumulate >= timer) {
            isActive = false
            reset()
            true
        } else false
    }

    fun reset() = kotlin.run { currentAccumulate = 0f }
    fun resetAll() = kotlin.run { timer = 0f; currentAccumulate = 0f }

    companion object {
        private val logger by lazy { loggerApp<DeltaTimerUp>() }
    }
}