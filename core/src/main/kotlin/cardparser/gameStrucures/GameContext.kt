package cardparser.gameStrucures

import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.systems.parts.screeninput.CardMoveProcessor.TouchStatus

@Deprecated("global state isn't very good idea")
object GameContext {
    val touchList : MutableList<GameCardAdapter> = mutableListOf()
    var touchListStatus: TouchStatus = TouchStatus.NONE
}
