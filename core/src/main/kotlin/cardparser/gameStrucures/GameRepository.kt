package cardparser.gameStrucures

import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.components.adapters.GameStackAdapter
import com.badlogic.gdx.math.Vector2

/**
 * Class for comfortable store and work with GameCards & GameStacks
 */
class GameRepository(
        val cards: List<GameCardAdapter>,
        val stacks: List<GameStackAdapter>
) {
    fun getCard(index: Int): GameCardAdapter = cards[index]
    fun getStack(index: Int): GameStackAdapter = stacks[index]

    /**
     * Find card and it's stack by [pos].
     *
     * @note can't find card that is outside of any stack.
     * @return null -> if pos is outside of any stack.
     */
    fun findPair(pos: Vector2): Pair<GameStackAdapter, GameCardAdapter>? {
        stacks.find { it.containsPosInTotalHitBox(pos) }?.let { stack ->
            stack.gameStackComp.findCardByPos(pos)?.let { card -> return Pair(stack, card) }
        }
        return null
    }

    /** Find card by [pos] in all game cards. */
    fun findCard(pos: Vector2): GameCardAdapter? {
        return cards.filter { it.touchComp.isTouchable && it.transComp.shape.contains(pos) }
                .maxByOrNull { it.transComp.position.z }
    }

    fun findStack(pos: Vector2): GameStackAdapter? = stacks.find { it.containsPos(pos) }

    fun findStack(card: GameCardAdapter): GameStackAdapter? {
        return stacks.find { it.gameStackComp.contains(card) }
    }


}