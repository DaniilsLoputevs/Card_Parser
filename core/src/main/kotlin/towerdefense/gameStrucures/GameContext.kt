package towerdefense.gameStrucures

import com.badlogic.ashley.core.Entity

import towerdefense.gameStrucures.DragAndDropManager.DragAndDropStatus.*

class GameContext {
    lateinit var stacks: Array<Entity>

    lateinit var cards: Array<Entity>


    /* DragAndDrop part*/

    var dndSelectedEntity: Entity? = null
    /** DragAndDrop status */
    var dndEntityStatus: DragAndDropManager.DragAndDropStatus = NONE

}