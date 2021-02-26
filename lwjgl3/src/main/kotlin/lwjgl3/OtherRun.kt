package lwjgl3

//fun main() {
//        var t:Int = Class().someFun()
//
//    var a = Lwjgl3ApplicationConfiguration().apply {
//        setTitle("tower_defense")
//        setWindowedMode(640, 480)
//        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
//    }
//}

inline fun Class.someFun() :Int = 12

class Class {

}


fun main() {
    val test = MyClass(arrayOf("January", "February", "March"))
    println(test[0])
    println(test["Just srt"])

    val cont: Container<String> = Container("STR")
    val rsl = cont.show {}
    println(rsl)
}

class MyClass(var arr: Array<String>) {

    operator fun get(index: Int): String {
        return arr[index]
    }

    operator fun get(str: String): String {
        return str
    }
}

class Container<T>(var content: T) {

}

inline fun <T> Container<T>.show(param: Any): String {
    return this.content.toString();
}
