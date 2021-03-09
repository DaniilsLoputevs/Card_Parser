package towerdefense.gameStrucures

import com.badlogic.ashley.core.Entity

import towerdefense.gameStrucures.DragAndDropManager.DragAndDropStatus.*

class GameContext {
    lateinit var stacks: Array<Entity>

    lateinit var cards: Array<Entity>


    /* DragAndDrop part*/

    lateinit var dndSelectedEntity: Entity
    /** DragAndDrop status */
    var dndDragAndDropStatus: DragAndDropManager.DragAndDropStatus = NONE

}