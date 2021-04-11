package towerdefense.gameStrucures

import towerdefense.ashley.systems.parts.screeninput.CardMoveProcessor
import towerdefense.ashley.systems.parts.screeninput.CardMoveProcessor.TouchStatus.NONE
import towerdefense.gameStrucures.adapters.GameCardAdapter

object GameContext {
    val touchList : MutableList<GameCardAdapter> = mutableListOf()
    var touchListStatus: CardMoveProcessor.TouchStatus = NONE
}
