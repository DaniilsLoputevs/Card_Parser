package lwjgl3

import com.badlogic.ashley.core.Entity
import towerdefense.ashley.components.game.GameCardComponent
import towerdefense.ashley.components.game.GameStackComponent


fun main() {

    var s : String? = find("asd")
//    s = null

    s?.let {
        println("before: $it")
    }

    println("after: $s")

















//    val t = MyClass()
//    println(t.isEmpty())
//    println(t.getLastCard())
//    println(t.getLastCard())
//    println(t.getLastCard())
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

