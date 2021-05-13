package cardparser.ashley.entities

import cardparser.ashley.components.*

class MainStack : MainStackAPI, StackAPI, GameEntity() {
    override lateinit var transComp: TransformComp
    override lateinit var mainStackComp: MainStackComp
    override lateinit var stackComp: StackComp
}