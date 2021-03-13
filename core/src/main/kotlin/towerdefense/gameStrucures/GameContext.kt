package towerdefense.gameStrucures

import towerdefense.gameStrucures.CardMoveProcessor.DragAndDropStatus.NONE
import towerdefense.gameStrucures.adapters.GameCardAdapter
import towerdefense.gameStrucures.adapters.GameStackAdapter

class GameContext {
    lateinit var stacks: List<GameStackAdapter>
    lateinit var cards: List<GameCardAdapter>


    /* DragAndDrop part*/

    var dndSelectedCard: GameCardAdapter? = null

    /** DragAndDrop status */
    var dndEntityStatus: CardMoveProcessor.DragAndDropStatus = NONE

}