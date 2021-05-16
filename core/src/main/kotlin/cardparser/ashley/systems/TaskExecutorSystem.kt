package cardparser.ashley.systems

import cardparser.event.TaskManager
import com.badlogic.ashley.core.EntitySystem

class TaskExecutorSystem : EntitySystem() {

    override fun update(deltaTime: Float) {
        TaskManager.update()
    }
}