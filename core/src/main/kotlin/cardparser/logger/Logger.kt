package cardparser.logger

import java.util.*

inline fun <reified C : Any> loggerApp(): Logger<C> = LoggerImpl(C::class.java.simpleName)

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


/* Logger printing templates */
private const val LOG_INF_TEMPLATE = "*LEVEL* || *CLASS* => *MSG*"
private const val LOG_VAR_TEMPLATE = "*LEVEL* || *CLASS* => *MSG* :: *OBJ*"

/* symbols for replace */
private val specSymbols: Map<String, String> by lazy {
    mapOf(
            Pair(" = ", "$TEXT_RED = $TEXT_RESET"),
            Pair(" : ", "$TEXT_RED : $TEXT_RESET"),
            Pair("::", "$TEXT_RED::$TEXT_RESET"),
            Pair("=>", "$TEXT_BLUE=>$TEXT_RESET"),
    )
}


class LoggerImpl<C>(clazz: String) : Logger<C> {
    private val levels = mutableListOf<LogLevel>().apply { this.addAll(LogLevel.values()) }
    private val infTemplate = LOG_INF_TEMPLATE.replace("*CLASS*", clazz)
    private val valTemplate = LOG_VAR_TEMPLATE.replace("*CLASS*", clazz)


    override fun dev(msg: String) = check(LogLevel.DEV) { println(of(LogLevel.DEV, msg)) }
    override fun dev(msg: String, obj: Any) = check(LogLevel.DEV) { println(of(LogLevel.DEV, msg, obj)) }

    override fun debug(msg: String) = check(LogLevel.DEBUG) { println(of(LogLevel.DEBUG, msg)) }
    override fun debug(msg: String, obj: Any) = check(LogLevel.DEBUG) { println(of(LogLevel.DEBUG, msg, obj)) }

    override fun info(msg: String) = check(LogLevel.INFO) { println(of(LogLevel.INFO, msg)) }
    override fun info(msg: String, obj: Any) = check(LogLevel.INFO) { println(of(LogLevel.INFO, msg, obj)) }

    override fun warm(msg: String) = check(LogLevel.WARM) { println(of(LogLevel.WARM, msg)) }
    override fun warm(msg: String, obj: Any) = check(LogLevel.WARM) { println(of(LogLevel.WARM, msg, obj)) }

    override fun error(msg: String) = check(LogLevel.ERROR) { println(of(LogLevel.ERROR, msg)) }
    override fun error(msg: String, obj: Any) = check(LogLevel.ERROR) { println(of(LogLevel.ERROR, msg, obj)) }

    override fun levels(vararg levels: LogLevel) {
        this.levels.clear(); this.levels.addAll(levels)
    }


    private fun check(level: LogLevel, print: () -> Unit) = if (levels.contains(level)) print.invoke() else Unit

    private fun of(level: LogLevel, msg: String): String = buildInfo(level, msg)
    private fun of(level: LogLevel, msg: String, obj: Any): String {
        return when (obj) {
            is Iterable<*> -> makeIterableMsg(level, msg, obj as Iterable<Any>)
            is Map<*, *> -> makeMapMsg(level, msg, obj as Map<Any, Any>)
            else -> buildValue(level, msg, wrap(obj))
        }
    }

    private fun makeIterableMsg(level: LogLevel, iterName: String, iterable: Iterable<Any>): String {
        val rsl = StringJoiner(System.lineSeparator())
        rsl.add(buildValue(level, "Iterable", iterName))
        if (iterable.none()) rsl.add(buildInfo(level, "Iterable is empty"))
        else iterable.forEachIndexed { i, elem -> rsl.add(buildValue(level, "$i", wrap(elem))) }
        return rsl.toString()
    }

    private fun makeMapMsg(level: LogLevel, mapName: String, map: Map<Any, Any>): String {
        val rsl = StringJoiner(System.lineSeparator())
        rsl.add(buildValue(level, "Map", mapName))
        if (map.isEmpty()) rsl.add(buildInfo(level, "Map is empty"))
        else map.onEachIndexed { i, (k, v) -> rsl.add(buildValue(level, "$i", "key=${wrap(k)} && val=${wrap(v)}")) }
        return rsl.toString()
    }

    private fun buildInfo(level: LogLevel, msg: String): String {
        return infTemplate
                .replace("*LEVEL*", level.toString())
                .replace("*MSG*", msg)
                .replaceSpecialSymbols()
    }

    private fun buildValue(level: LogLevel, msg: String, any: Any): String {
        return valTemplate
                .replace("*LEVEL*", level.toString())
                .replace("*MSG*", msg)
                .replace("*OBJ*", any.toString())
                .replaceSpecialSymbols()
    }

    private fun wrap(obj: Any): String {
        return if (obj is String) "\"" + obj + "\""
        else obj.toString()
    }

    private fun String.replaceSpecialSymbols(): String {
        var rsl = this
        specSymbols.forEach { (key, value) -> rsl = rsl.replace(key, value) }
        return rsl
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