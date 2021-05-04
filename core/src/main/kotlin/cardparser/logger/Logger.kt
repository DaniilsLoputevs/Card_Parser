package cardparser.logger

import java.util.*

// Define color constants
private const val TEXT_RESET = "\u001B[0m"
private const val TEXT_BLACK = "\u001B[30m"
private const val TEXT_RED = "\u001B[31m"
private const val TEXT_GREEN = "\u001B[32m"
private const val TEXT_YELLOW = "\u001B[33m"
private const val TEXT_BLUE = "\u001B[34m"
private const val TEXT_PURPLE = "\u001B[35m"
private const val TEXT_CYAN = "\u001B[36m"
private const val TEXT_WHITE = "\u001B[37m"

/* Printing templates */
private const val LOG_INFO_TEMPLATE = "*LEVEL* || *CLASS* $TEXT_BLUE=>$TEXT_RESET *MSG*"
private const val LOG_VAR_TEMPLATE = "*LEVEL* || *CLASS* $TEXT_BLUE=>$TEXT_RESET *MSG* $TEXT_RED::$TEXT_RESET *OBJ*"

/**
 * TODO - class ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - levels ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - template ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - colour ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - smart cast for args ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - smart cast extensions strategy ??? ToString() ??? - old ^^^^^
 * TODO - pretty wrap: single obj ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - pretty wrap: store obj ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - core ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - ? variable replacement ? ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - ? Multithreading ? ------------------------------------------
 * TODO - level API ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * TODO - msg template name
 */

/**
 * Logger for dev & debug prints.
 *
 * @author Daniils Loputevs
 * @version 3.0. (04.05.2021)
 */
interface Logger<C> {
    fun dev(msg: () -> String) = dev(msg.invoke())
    fun dev(msg: String)
    fun dev(msg: String, obj: Any)

    fun debug(msg: () -> String) = debug(msg.invoke())
    fun debug(msg: String)
    fun debug(msg: String, obj: Any)

    fun info(msg: () -> String) = info(msg.invoke())
    fun info(msg: String)
    fun info(msg: String, obj: Any)

    fun warm(msg: () -> String) = warm(msg.invoke())
    fun warm(msg: String)
    fun warm(msg: String, obj: Any)

    fun error(msg: () -> String) = error(msg.invoke())
    fun error(msg: String)
    fun error(msg: String, obj: Any)

    fun levels(vararg levels: LogLevel)
}

inline fun <reified C : Any> logger(): Logger<C> = LoggerImpl(C::class.java.simpleName)


class LoggerImpl<C>(clazz: String) : Logger<C> {
    private val levels = mutableListOf<LogLevel>().apply { this.addAll(LogLevel.values()) }

    private val LOG_INFO = LOG_INFO_TEMPLATE.replace("*CLASS*", clazz)
    private val LOG_VALUE = LOG_VAR_TEMPLATE.replace("*CLASS*", clazz)


    override fun dev(msg: String) = if (check(LogLevel.DEV)) println(of(LogLevel.DEV, msg)) else Unit
    override fun dev(msg: String, obj: Any) = if (check(LogLevel.DEV)) println(of(LogLevel.DEV, msg, obj)) else Unit

    override fun debug(msg: String) = if (check(LogLevel.DEBUG)) println(of(LogLevel.DEBUG, msg)) else Unit
    override fun debug(msg: String, obj: Any) = if (check(LogLevel.DEBUG)) println(of(LogLevel.DEBUG, msg, obj)) else Unit

    override fun info(msg: String) = if (check(LogLevel.INFO)) println(of(LogLevel.INFO, msg)) else Unit
    override fun info(msg: String, obj: Any) = if (check(LogLevel.INFO)) println(of(LogLevel.INFO, msg, obj)) else Unit

    override fun warm(msg: String) = if (check(LogLevel.WARM)) println(of(LogLevel.WARM, msg)) else Unit
    override fun warm(msg: String, obj: Any) = if (check(LogLevel.WARM)) println(of(LogLevel.WARM, msg, obj)) else Unit

    override fun error(msg: String) = if (check(LogLevel.ERROR)) println(of(LogLevel.ERROR, msg)) else Unit
    override fun error(msg: String, obj: Any) = if (check(LogLevel.ERROR)) println(of(LogLevel.ERROR, msg, obj)) else Unit

    override fun levels(vararg levels: LogLevel) {
        this.levels.clear()
        this.levels.addAll(levels)
    }

    private fun check(level: LogLevel): Boolean = levels.contains(level)
    private fun of(level: LogLevel, msg: String): String = buildInfo(level, msg)
    private fun of(level: LogLevel, msg: String, obj: Any): String {
        return when (obj) {
            is List<*> -> makeIterableMsg(level, msg) { obj as List<Any>; obj.iterator() }
            is Map<*, *> -> makeMapMsg(level, msg, obj as Map<Any, Any>)
            is Array<*> -> makeIterableMsg(level, msg) { obj as Array<Any>; obj.iterator() }
            else -> buildValue(level, msg, wrap(obj))
        }
    }

    private fun makeIterableMsg(level: LogLevel, iterName: String, iter: () -> Iterator<Any>): String {
        val rsl = StringJoiner(System.lineSeparator())
        rsl.add(buildValue(level, "Iterable", iterName))
        val iterator = iter.invoke()
        var elemIndex = 0
        if (!iterator.hasNext()) rsl.add(buildInfo(level, "Iterable is empty"))
        else iterator.forEach { rsl.add(buildValue(level, elemIndex++.toString(), wrap(it))) }
        return rsl.toString()
    }

    private fun makeMapMsg(level: LogLevel, mapName: String, map: Map<Any, Any>): String {
        val rsl = StringJoiner(System.lineSeparator())
        rsl.add(buildValue(level, "Map", mapName))
        if (map.isNotEmpty()) {
            var elemIndex = 0
            map.forEach { (key, value) ->
                rsl.add(buildValue(level, elemIndex++.toString(), "key=${wrap(key)} && val=${wrap(value)}"))
            }
        } else rsl.add(buildInfo(level, "Map is empty"))
        return rsl.toString()
    }

    private fun buildInfo(level: LogLevel, msg: String): String {
        return LOG_INFO
                .replace("*LEVEL*", level.toString())
                .replace("*MSG*", msg)
                .replace("::", "$TEXT_RED::$TEXT_RESET")
    }

    private fun buildValue(level: LogLevel, msg: String, any: Any): String {
        return LOG_VALUE
                .replace("*LEVEL*", level.toString())
                .replace("*MSG*", msg)
                .replace("*OBJ*", any.toString())
    }

    private fun wrap(obj: Any): String {
        return if (obj is String) {
            "\"" + obj + "\""
        }
        else obj.toString()
//        return obj.toString()
    }

}

enum class LogLevel {
    DEV {
        override fun toString(): String = "${TEXT_CYAN}DEV${TEXT_RESET}"
    },
    DEBUG {
        override fun toString(): String = "${TEXT_PURPLE}DEBUG${TEXT_RESET}"
    },
    INFO {
        override fun toString(): String = "${TEXT_GREEN}INFO${TEXT_RESET}"
    },
    WARM {
        override fun toString(): String = "${TEXT_YELLOW}WARM${TEXT_RESET}"
    },
    ERROR {
        override fun toString(): String = "${TEXT_RED}ERROR${TEXT_RESET}"
    },
}

