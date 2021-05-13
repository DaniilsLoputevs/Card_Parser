package cardparser.ashley.systems

import cardparser.ashley.entities.Card
import cardparser.ashley.entities.Stack
import cardparser.logger.loggerApp
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class DebugSystem : EntitySystem() {

    lateinit var cards: MutableList<Card>
    lateinit var mainStack: Stack
    lateinit var bottomStacks: List<Stack>
    lateinit var upStacks: List<Stack>


    /**
     * N -> cards
     * M -> main stack
     * B -> bottom stacks
     * V -> up stacks
     */
    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            println()
            logger.debug("CARDS")
            cards.forEach { logger.debug(it.toString()) }
            logger.debug("CARDS")
            println()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            println()
            logger.debug("MAIN STACK")
            logger.debug(mainStack.toString())
            logger.debug("MAIN STACK")
            println()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            println()
            logger.debug("BOTTOM STACKS")
            bottomStacks.forEach { logger.debug(it.toString()) }
            logger.debug("BOTTOM STACKS")
            println()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            println()
            logger.debug("FOUNDATION STACKS")
            upStacks.forEach { logger.debug(it.toString()) }
            logger.debug("FOUNDATION STACKS")
            println()
        }
    }

    companion object {
        private val logger = loggerApp<DebugSystem>()
    }
}
