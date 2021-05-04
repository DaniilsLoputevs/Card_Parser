package cardparser.logger

import java.util.*

/**
 * Custom Logger for dev & debug prints.
 *
 * @author Daniils Loputevs
 * @version 2.0.
 */
object LogTemplateRun {
    // Define color constants
    const val TEXT_RESET = "\u001B[0m"
    const val TEXT_BLACK = "\u001B[30m"
    const val TEXT_RED = "\u001B[31m"
    const val TEXT_GREEN = "\u001B[32m"
    const val TEXT_YELLOW = "\u001B[33m"
    const val TEXT_BLUE = "\u001B[34m"
    const val TEXT_PURPLE = "\u001B[35m"
    const val TEXT_CYAN = "\u001B[36m"
    const val TEXT_WHITE = "\u001B[37m"

    /* Printing templates */
    private const val DEV_LOG_INF = "DEV : [*place*] -- *info*"
    private const val DEV_LOG_VAR = "DEV : [*place*] -- *info* :: *obj*"
    private const val ELEM_STORE_PRINT_START = "DEV : [*place*] -- Print *store* :: *name*"
    private const val ITERABLE_ELEM = "DEV : [*place*] -- *index* :: *elem*"
    private const val MAP_ELEM = "DEV : [*place*] -- *index* :: key=*key* ### val=*val*"

    //    public static void main(String[] args) {
    //
    //
    //        // Implementation
    //        System.out.println(TEXT_CYAN + "This text is red!" + TEXT_RESET + " --- Reset Text" + TEXT_BLUE + " New Text");
    //
    //
    ////        print("test", "myList", List.of("one", "two"));
    //
    //
    ////        print("test", "myMap", Map.of("Sajren", 143, "Kirin", 100));
    ////        print("test", "SB", new KafkaProperties.Security[12]);
    //    }
    fun print(place: String, info: String) = println(makeInfoMsg(place, info))
    fun printErr(place: String, info: String) = System.err.println(makeInfoMsg(place, info))
    fun print(place: String, info: String, obj: Any) = println(printStrategy(place, info, obj))


    fun printErr(place: String, info: String, obj: Any) {
        System.err.println(printStrategy(place, info, obj))
    }

    private fun printStrategy(place: String, info: String, obj: Any): String {
        return when (obj) {
            is List<*> -> makeListMsg(place, info, obj as List<Any>)
            is Map<*, *> -> makeMapMsg(place, info, obj as Map<Any, Any>)
            is Array<*> -> makeArrayMsg(place, info, obj as Array<Any>)
            else -> makeObjectMsg(place, info, obj)
        }
    }

    private fun makeInfoMsg(place: String, info: String): String {
        return DEV_LOG_INF
                .replace("*place*", upperCasePlace(place))
                .replace("*info*", info)
    }

    private fun makeObjectMsg(place: String, info: String, obj: Any): String {
        return DEV_LOG_VAR
                .replace("*place*", upperCasePlace(place))
                .replace("*info*", info)
                .replace("*obj*", prettyWrapObject(obj))
    }

    private fun makeArrayMsg(place: String, arrayName: String, arr: Array<Any>): String {
        var place = place
        val rsl = StringJoiner(System.lineSeparator())
        place = upperCasePlace(place)
        rsl.add(ELEM_STORE_PRINT_START
                .replace("*place*", place)
                .replace("*store*", "Array")
                .replace("*name*", arrayName)
        )
        if (arr.isNotEmpty()) {
            val localTemplate = ITERABLE_ELEM.replace("*place*", place)
            var elemIndex = 0
            for (obj in arr) {
                rsl.add(localTemplate
                        .replace("*index*", elemIndex++.toString())
                        .replace("*elem*", prettyWrapObject(obj))
                )
            }
        }
        return rsl.toString()
    }

    private fun makeListMsg(place: String, listName: String, list: List<Any>): String {
        var place = place
        val rsl = StringJoiner(System.lineSeparator())
        place = upperCasePlace(place)
        rsl.add(ELEM_STORE_PRINT_START
                .replace("*place*", place)
                .replace("*store*", "List")
                .replace("*name*", listName)
        )
        if (!list.isEmpty()) {
            val localTemplate = ITERABLE_ELEM.replace("*place*", place)
            var elemIndex = 0
            for (obj in list) {
                rsl.add(localTemplate
                        .replace("*index*", elemIndex++.toString())
                        .replace("*elem*", prettyWrapObject(obj))
                )
            }
        }
        return rsl.toString()
    }

    private fun makeMapMsg(place: String, mapName: String, map: Map<Any, Any>): String {
        val rsl = StringJoiner(System.lineSeparator())
        rsl.add(ELEM_STORE_PRINT_START
                .replace("*place*", upperCasePlace(place))
                .replace("*store*", "Map")
                .replace("*name*", mapName)
        )
        if (!map.isEmpty()) {
            val localTemplate = MAP_ELEM.replace("*place*", place)
            var elemIndex = 0
            for ((key, value) in map) {
                rsl.add(localTemplate
                        .replace("*index*", elemIndex++.toString())
                        .replace("*key*", prettyWrapObject(key))
                        .replace("*val*", prettyWrapObject(value))
                )
            }
        }
        return rsl.toString()
    }

    /**
     * Need for Easy extend object formatting, just add `else if`.
     * *not for List, it's for <elementType>.
    </elementType> */
    private fun prettyWrapObject(obj: Any): String {
        return if (obj is String) {
            "\"" + obj + "\""
        } else {
            obj.toString()
        }
    }

    private fun upperCasePlace(place: String): String {
        return place.substring(0, 1).toUpperCase() + place.substring(1)
    }
}