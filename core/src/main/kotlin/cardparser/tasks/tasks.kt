package cardparser.tasks

import cardparser.logger.loggerApp

interface Task {
    val taskId: Int
    var isFinished: Boolean

    fun update()
}


object TaskManager {
    private var nextTaskId: Int = 0
    private val tasks: MutableList<Task> = mutableListOf()
    private val commitHistory: MutableList<String> = mutableListOf()

    fun genId() = nextTaskId++

    fun commit(task: Task) {
        tasks.add(task)
        commitHistory += task.toString()
    }

    fun update() {
        if (tasks.isEmpty()) return
//        logger.info("TM update -- current tasks", tasks)

//        logger.warm("commit history", commitHistory)
        updateEachTask()
//        logger.warm("tasks size", tasks.size)
//        logger.dev("finished task", tasks.filter { it.isFinished })
        tasks.removeAll { it.isFinished }
//        logger.warm("tasks size", tasks.size)
    }

    /**
     * Use Iterator way -> java.util.ConcurrentModificationException
     * * Cause Task can commit new Task and it change collection inner state while iterating
     *
     *  for ((i, e) in list.withIndex()) {
     *  logger.info("$i", e); list.add(i + 5)
     *  } ->> java.util.ConcurrentModificationException
     */
    private fun updateEachTask() {
        for (i in 0..tasks.lastIndex) tasks[i].update()
    }

    fun logHistory() = logger.error("commit history", commitHistory)
    fun sudoClear() = tasks.clear()
    fun size() = tasks.size

    private val logger by lazy { loggerApp<TaskManager>() }
}