package lwjgl3

import cardparser.logger.loggerApp
import com.badlogic.gdx.math.Vector2
import kotlin.math.roundToInt


fun main() {

    val list = mutableListOf<String>("a", "b", "c", "d", "e")
    val rsl = mutableListOf<String>()

    for (index in list.lastIndex downTo 0) rsl.add(list[index])

    logger.dev("rsl", rsl)


//    list.remove("a")
//    list.remove("y")
//    println(list[2])
//    println("size = ${list.size}")


//    val a = 1047.9999f
//    val b = 1048.0f
//
//    val d = 250.00002
//    val e = 250.0f
//
//    logger.dev("one", a.compareTo(b))
//    logger.dev("two", d.compareTo(e))
//
//    logger.dev("a", a.roundToInt())
//    logger.dev("d", d.roundToInt())
//    val r = "\\"
    val t = "\\033[6mHello world"

    println(t);

    println("$BACKGROUND_GREEN My text $TEXT_RESET new text")
    println("asdasdasd")
}




private const val posZ: Float = 100f

fun pos(pos: Vector2, z: Float = posZ) {}
fun pos(x: Float, y: Float, z: Float = 444444f) = println(x + y)


private const val TEXT_RESET = "\u001B[0m"
private const val TEXT_BLACK = "\u001B[30m"
private const val TEXT_RED = "\u001B[31m"
private const val TEXT_GREEN = "\u001B[32m"
private const val TEXT_YELLOW = "\u001B[33m"
private const val TEXT_BLUE = "\u001B[34m"
private const val TEXT_PURPLE = "\u001B[35m"
private const val TEXT_CYAN = "\u001B[36m"
private const val TEXT_WHITE = "\u001B[37m"


private const val BACKGROUND_BLACK = "\u001B[40m"
private const val BACKGROUND_RED = "\u001B[41m"
private const val BACKGROUND_GREEN = "\u001B[42m"
private const val BACKGROUND_YELLOW = "\u001B[43m"
private const val BACKGROUND_BLUE = "\u001B[44m"
private const val BACKGROUND_MAGENTA = "\u001B[45m"
private const val BACKGROUND_CYAN = "\u001B[46m"
private const val BACKGROUND_WHITE = "\u001B[47m"

private const val BACKGROUND_RESET = "\u001B[47m" // Make msg background

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

private val logger = loggerApp<OtherRun>()
class OtherRun {}