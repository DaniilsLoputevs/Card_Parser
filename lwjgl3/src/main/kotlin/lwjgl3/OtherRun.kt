package lwjgl3

import towerdefense.ashley.components.klondike.GameCardComponent


fun main() {

    println("rsl = ${GameCardComponent.CardRank.KING > GameCardComponent.CardRank.FIVE}")

    val list = mutableListOf<String>("111", "22", "3")
    list.reverse()
    println(list)

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

