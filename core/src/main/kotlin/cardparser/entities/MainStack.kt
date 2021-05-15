package cardparser.entities

import cardparser.ashley.components.MainStackAPI
import cardparser.ashley.components.MainStackComp
import cardparser.ashley.components.StackComp
import cardparser.ashley.components.TransformComp
import com.badlogic.ashley.core.Entity

class MainStack() : MainStackAPI, Stack() {
    override lateinit var transComp: TransformComp
    override lateinit var mainStackComp: MainStackComp
    override lateinit var stackComp: StackComp

    /* inner part */
    constructor(entity: Entity) : this() {
        this.entity = entity
    }

    override fun toString(): String = "main stack={ pos = ${this.pos()} & size = ${this.size()} }"
}