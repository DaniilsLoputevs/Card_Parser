package lwjgl3


fun main() {
//    val t = 6
//
//    if (t > 10) {
//        println("more 10")
//        if (t < 100) {
//            println("less 100")
//            if (t == 20) {
//                println("equal 20")
//            } else if (t == 30) {
//                println("equal 30")
//            }
//        }
//    }
//
//
//    if (t > 10) println("more 10")
//    if (t > 10 && t < 100) println("less 100")
//    if ((t > 10 && t < 100) && (t == 20)) println("equal 20")
//    if ((t > 10 && t < 100) && (t == 20)) println("equal 30")


    val adapterTest = AdapterTest()
    val adapterTestArg = AdapterTest("tup")

    adapterTest.entity = "yup"
    println("one = ${adapterTest.one}")
    println("two = ${adapterTest.two}")
    adapterTest.entity = "Kot"
    println("one = ${adapterTest.one}")
    println("two = ${adapterTest.two}")

}

public val DEFAULT_ENTITY = ""

class AdapterTest() : AbstractAdapter() {
    lateinit var one: String
    lateinit var two: String


    constructor(entity: String) : this() {this.entity = entity }

    override fun refreshState() {
        one = entity.substring(0, 1)
        two = entity.substring(1, 2)
    }
}

abstract class AbstractAdapter(entity: String = DEFAULT_ENTITY) {
    var entity: String = DEFAULT_ENTITY
        set(value) {
            field = value
            refreshState()
        }

    abstract fun refreshState()
}





fun find(s: String): String? {
    return s
}

class MyClass {
    var cardStack: MutableList<String> = mutableListOf("asd", "www")

//    fun isEmpty() = cardStack.isEmpty()
//    fun addGameCard(card : Entity) = cardStack.add(card)
//
//    fun getLastCard() = cardStack.removeAt(cardStack.size.coerceAtMost(0))


}

