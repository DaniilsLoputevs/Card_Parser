package towerdefense.gameStrucures

import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.NONE
import towerdefense.gameStrucures.adapters.GameCardAdapter

class GameContext {


    var touchingCard: GameCardAdapter? = null
    var touchingCardStatus: CardMoveProcessor.DragAndDropStatus = NONE



}