package lwjgl3


fun main() {


    val template = "*LEVEL* || [*CLASS*] $TEXT_BLUE=>$TEXT_RESET *INFO* $TEXT_RED::$TEXT_RESET *OBJ*"
    println(template
            .replace("*LEVEL*", "DEV")
            .replace("*CLASS*", "GameStackAdapter")
            .replace("*INFO*", "value")
            .replace("*OBJ*", "1343523412")
    )
}


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

