package lwjgl3

import towerdefense.ashley.components.game.GameCardComponent


fun main() {

    println("rsl = ${GameCardComponent.CardRank.KING > GameCardComponent.CardRank.FIVE}")



}

fun find(s : String) : String? {
    return s
}

class MyClass {
    var cardStack: MutableList<String> = mutableListOf("asd", "www")

//    fun isEmpty() = cardStack.isEmpty()
//    fun addGameCard(card : Entity) = cardStack.add(card)
//
//    fun getLastCard() = cardStack.removeAt(cardStack.size.coerceAtMost(0))


}

