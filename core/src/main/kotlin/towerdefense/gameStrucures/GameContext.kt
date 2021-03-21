package towerdefense.gameStrucures

import towerdefense.gameStrucures.CardMoveProcessor.TouchStatus.NONE
import towerdefense.gameStrucures.adapters.GameCardAdapter

class GameContext {

//    var touchingCard: GameCardAdapter? = null
//    var touchCardStatus: CardMoveProcessor.TouchStatus = NONE


    val touchList : MutableList<GameCardAdapter> = mutableListOf()
    var touchListStatus: CardMoveProcessor.TouchStatus = NONE
}
