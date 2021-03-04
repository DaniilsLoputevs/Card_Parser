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

inline fun Class.someFun(): Int = 12

class Class {

}


fun main() {
//    val height = 480f
//    val width = 342f
//    val textureAspectRation = (height / width)
//    println("textureAspectRation = $textureAspectRation")
//    val newHeight = 140
//    println("newHeight = $newHeight && newWidth = ${newHeight * textureAspectRation}")


    val height = 750f
    val width = 500f
    val ratio = height / width // (750 / 500)

    val newHeight = 1500
    val newWidth = newHeight / ratio
    println("newWidth = $newWidth")




//    val test = MyClass(arrayOf("January", "February", "March"))
//    println(test[0])
//    println(test["Just srt"])
//
//    val cont: Container<String> = Container("STR")
//    val rsl = cont.show {}
//    println(rsl)
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
