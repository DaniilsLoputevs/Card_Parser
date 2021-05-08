package lwjgl3

import cardparser.logger.loggerApp
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2

class OtherRun {}

private val logger = loggerApp<OtherRun>()

fun main() {


    val vec = Vector3(1f, 2f, 3f)

    val (a, b) = vec
    logger.dev("a") { a }
    logger.dev("b") { b }
//    logger.dev("c") { c }

    pos(vec2(a, b))

//    val l :MutableList<String> = mutableListOf("aaa", "bbb", "ccc").shuffle(Random()) as MutableList<String>
//    val t = l.shuffled(Random())

//    logger.dev("list", l)
//    logger.dev("other", t)
}

private const val posZ : Float = 100f

fun pos(pos : Vector2, z: Float = posZ ) {}
fun pos(x : Float, y : Float, z : Float = 444444f) = println( x + y)


private const val TEXT_RESET = "\u001B[0m"
private const val TEXT_BLACK = "\u001B[30m"
private const val TEXT_RED = "\u001B[31m"
private const val TEXT_GREEN = "\u001B[32m"
private const val TEXT_YELLOW = "\u001B[33m"
private const val TEXT_BLUE = "\u001B[34m"
private const val TEXT_PURPLE = "\u001B[35m"
private const val TEXT_CYAN = "\u001B[36m"
private const val TEXT_WHITE = "\u001B[37m"

class MyClass {
    var cardStack: MutableList<String> = mutableListOf("asd", "www")

    fun t() {
//        logger.
    }
//    fun isEmpty() = cardStack.isEmpty()
//    fun addGameCard(card : Entity) = cardStack.add(card)
//
//    fun getLastCard() = cardStack.removeAt(cardStack.size.coerceAtMost(0))

    companion object {
//        val logger = ""
    }

}

