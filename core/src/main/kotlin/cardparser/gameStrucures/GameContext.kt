package cardparser.gameStrucures

import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.ashley.systems.parts.screeninput.CardMoveProcessor.TouchStatus

object GameContext {
    val touchList : MutableList<GameCardAdapter> = mutableListOf()
    var touchListStatus: TouchStatus = TouchStatus.NONE
}
