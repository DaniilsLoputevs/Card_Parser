package cardparser.ashley.systems

import cardparser.entities.Card
import cardparser.entities.MainStack
import cardparser.entities.Stack
import cardparser.event.GameEventManager
import cardparser.logger.loggerApp
import cardparser.scenario.startKlondikeGame
import cardparser.tasks.CalculateTouchable
import cardparser.tasks.GameActionHistory
import cardparser.tasks.TaskManager
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*

object Debug {
    lateinit var mainStack: Stack
    lateinit var subStack: MainStack
    lateinit var upStacks: List<Stack>
    lateinit var bottomStacks: List<Stack>
    lateinit var cards: MutableList<Card>

    val actions = mutableSetOf(
            Act(M, "CARDS") { cards.forEach { logger.debug(it.toString()) } },
            Act(N, "MAIN STACKS") { logger.debug(mainStack.toString()); logger.debug(subStack.toString()) },
            Act(B, "BOTTOM STACK") { bottomStacks.forEach { logger.debug(it.toString()) } },
            Act(V, "FOUNDATION STACKS") { upStacks.forEach { logger.debug(it.toString()) } },

            Act(L, "EVENTS HISTORY") { GameEventManager.logHistory() },
            Act(K, "TASK HISTORY") { TaskManager.logHistory() },
            Act(J, "ACTION HISTORY SIZE") { logger.debug("size", GameActionHistory.size()) },

            Act(Q, "QUIT") { throw ExceptionForQuit() },
            Act(R, "RESTART") { restartKlondikeGame() },
            Act(Z, "CANCEL LAST ACTION") { GameActionHistory.cancelLastAction() },

            Act(T, "SUDO TASKS CLEAR") { TaskManager.sudoClear() },
    )

    /**
     * You can set Debug action everywhere by using this:
     * Debug.act("J", "TEST HOTKEY") { logger.debug("msg", "obj")}
     *
     * @see [com.badlogic.gdx.Input].[toString(int key)] - for correct [key] value.
     */
    fun act(key: String, msg: String, act: () -> Unit) = actions.add(Act(valueOf(key), msg, act))


    private fun restartKlondikeGame() {
        GameActionHistory.clear()
        cards.forEachIndexed { i, it ->
            it.setPos(mainStack.pos().x, mainStack.pos().y, i.toFloat())
            it.open(false); it.touchable(false)
        }
        cards.shuffle()

        mainStack.clear()
        subStack.clear()
        bottomStacks.forEach(Stack::clear)
        upStacks.forEach(Stack::clear)

        startKlondikeGame()
        TaskManager.commit(CalculateTouchable(TaskManager.genId()))
    }

    data class Act(val key: Int, val msg: String, val act: () -> Unit)
    private class ExceptionForQuit : RuntimeException()
}

class DebugSystem : EntitySystem() {

    override fun update(deltaTime: Float) {
        Debug.actions.forEach {
            if (Gdx.input.isKeyJustPressed(it.key)) {
                println()
                logger.debug(it.msg)
                it.act.invoke()
                logger.debug(it.msg)
                println()
            }
        }

    }

}


private val logger by lazy { loggerApp<Debug>() }