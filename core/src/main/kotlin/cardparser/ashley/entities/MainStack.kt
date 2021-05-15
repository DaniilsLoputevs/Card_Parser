package cardparser.ashley.entities

import cardparser.ashley.components.*
import com.badlogic.ashley.core.Entity

class MainStack() : MainStackAPI, StackAPI, GameEntity() {
    override lateinit var transComp: TransformComp
    override lateinit var mainStackComp: MainStackComp
    override lateinit var stackComp: StackComp

    /* inner part */
    constructor(entity: Entity) : this() {
        this.entity = entity
    }

    override fun toString(): String = "main stack={ pos = ${this.pos()} & size = ${this.size()} }"
}