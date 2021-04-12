package cardparser.gameStrucures

import cardparser.ashley.systems.parts.screeninput.CardMoveProcessor
import cardparser.ashley.systems.parts.screeninput.CardMoveProcessor.TouchStatus.NONE
import cardparser.gameStrucures.adapters.GameCardAdapter

object GameContext {
    val touchList : MutableList<GameCardAdapter> = mutableListOf()
    var touchListStatus: CardMoveProcessor.TouchStatus = NONE
}
