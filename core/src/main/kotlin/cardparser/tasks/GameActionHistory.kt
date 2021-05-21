package cardparser.tasks

import cardparser.ACTION_HISTORY_SIZE
import cardparser.logger.loggerApp

object GameActionHistory {
    private val actions: MutableList<Task> = mutableListOf()

    fun addAction(backTask: Task) {
        if (actions.size == ACTION_HISTORY_SIZE) actions.removeFirstOrNull()
        actions.add(backTask)
    }

    fun cancelLastAction() {
        actions.removeLastOrNull()?.let {
            TaskManager.commit(it)
        } ?: logger.warm("actions is EMPTY")
    }

    fun size() = actions.size
    fun clear() = actions.clear()

}

private val logger by lazy { loggerApp<GameActionHistory>() }