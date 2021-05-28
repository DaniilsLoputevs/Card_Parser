package cardparser.tasks

import cardparser.DISTANCE_CARD_MOVE_SLOW
import cardparser.MAX_CARD_MOVE_SPEED_RATE
import cardparser.MIN_CARD_MOVE_SPEED_RATE
import cardparser.STACK_CLOSE_CARD_OFFSET
import cardparser.common.DeltaTimerDown
import cardparser.entities.Card
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils.lerp
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import kotlin.math.roundToInt

// regular
// ongoing Task
class CardPosition(override val taskId: Int) : Task {
    override var isFinished: Boolean = false

    /** sometimes this task can be endless but logical it's done(it's rounding decimal numbers problem).
     *  it hit performance, so we set timeout.*/
    private val timeoutTimer = DeltaTimerDown(1.5f)

    private var z = 0F
    private var nextZ = 0F
    private val nextPos: Vector2 = Vector2.Zero

    private var hasCacheStacks = false
    private val recalcStacks = mutableSetOf<Stack>()


    override fun update() {
        if (!hasCacheStacks) findStacksForRecalc(); hasCacheStacks = true
//        logger.dev("update stacks", recalcStacks)

        if (recalcStacks.isEmpty()) return

        val fineStacks = mutableSetOf<Stack>()

        recalcStacks.forEach { stack ->
            var step = 0F
            stack.forEachIndexed { index, card ->
                nextPos.set(stack.pos().x, stack.pos().y - step)
                if (useFastAnimation(nextPos, card.pos())) {
                    card.setPos(
                            lerp(card.pos().x, nextPos.x, MAX_CARD_MOVE_SPEED_RATE),
                            lerp(card.pos().y, nextPos.y, MAX_CARD_MOVE_SPEED_RATE),
                            z + 1
                    )
                } else {
                    nextZ = if (index == 0) card.pos().z + z
                    else {
                        val prevCardDepth = stack[index - 1].pos().z
                        if (prevCardDepth > card.pos().z) prevCardDepth + z
                        else card.pos().z + z
                    }

                    card.setPos(
                            lerp(card.pos().x, nextPos.x, MIN_CARD_MOVE_SPEED_RATE),
                            lerp(card.pos().y, nextPos.y, MIN_CARD_MOVE_SPEED_RATE),
                            nextZ
                    )
                }
                step += calcStep(card, stack)
                z++
//                logger.dev("card pos", card.pos())
//                logger.dev("nextPos", nextPos)
//                logger.dev("rsl", isCardOnPlace(card.pos(), nextPos))

                if (index == stack.cards().lastIndex && isCardOnPlace(card.pos(), nextPos)) fineStacks.add(stack)
            }
        }
//        logger.dev("fineStacks", fineStacks)
        recalcStacks.removeAll(fineStacks)
//        logger.dev("recalcStacks", recalcStacks)
//        logger.dev("finish predicate", recalcStacks.isEmpty())

        if (recalcStacks.isEmpty()) isFinished = true
        if (!timeoutTimer.update(Gdx.graphics.deltaTime)) isFinished = true
    }

    /** Real optimization */
    private fun findStacksForRecalc() {
        val requirementPos = Vector2()
        allStacks.forEach { stack ->
            var step = 0F
            for (card in stack.cards()) {
                requirementPos.set(stack.pos().x, stack.pos().y - step)
                if (!isCardOnPlace(card.pos(), requirementPos)) {
                    recalcStacks.add(stack); break
                }
                step += calcStep(card, stack)
            }
        }
    }


    private fun calcStep(card: Card, stack: Stack): Long {
        return if (card.open()) stack.stackComp.shiftStep
        else (stack.stackComp.shiftStep / STACK_CLOSE_CARD_OFFSET).toLong()
    }

    private fun isCardOnPlace(cardPos: Vector3, reqPos: Vector2) =
            (cardPos.x.roundToInt() == reqPos.x.roundToInt()) && (cardPos.y.roundToInt() == reqPos.y.roundToInt())

    private fun useFastAnimation(vect2: Vector2, vect3: Vector3): Boolean {
        return vect2.x - vect3.x <= DISTANCE_CARD_MOVE_SLOW && vect2.x - vect3.x >= -DISTANCE_CARD_MOVE_SLOW
                && vect2.y - vect3.y <= DISTANCE_CARD_MOVE_SLOW && vect2.y - vect3.y >= -DISTANCE_CARD_MOVE_SLOW
    }

    override fun toString(): String = "CardPosition :: $taskId"

    companion object {
        lateinit var allStacks: MutableList<Stack>

        private val logger by lazy { loggerApp<CardPosition>() }
    }
}
