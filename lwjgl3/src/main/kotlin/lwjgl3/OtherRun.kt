package lwjgl3

import com.badlogic.ashley.core.Entity
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.game.GameStackComponent


fun main() {

    val stack = GameStackComponent()
    val card = Entity().apply {
        addAndReturn(GameCardComponent())
    }
    val card2 = Entity().apply {
        addAndReturn(GameCardComponent())
    }
    stack.addGameCard(card)

    println(card)
    println(card2)
    println(stack.contains(card2))












//    val t = MyClass()
//    println(t.isEmpty())
//    println(t.getLastCard())
//    println(t.getLastCard())
//    println(t.getLastCard())
}

class MyClass {
    var cardStack: MutableList<String> = mutableListOf("asd", "www")

//    fun isEmpty() = cardStack.isEmpty()
//    fun addGameCard(card : Entity) = cardStack.add(card)
//
//    fun getLastCard() = cardStack.removeAt(cardStack.size.coerceAtMost(0))


}

