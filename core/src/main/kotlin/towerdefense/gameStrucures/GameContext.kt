package towerdefense.gameStrucures

import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.NONE
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

class GameContext {


    var touchingCard: GameCardAdapter? = null
    var touchStack : GameStackAdapter? = null
    var touchingCardStatus: CardMoveProcessor.DragAndDropStatus = NONE



}