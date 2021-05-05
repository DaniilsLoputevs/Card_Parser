package cardparser.logger

class LoggerTest {
    val value = "1234567"
}

private val logger = loggerApp<LoggerTest>().apply { this.levels(LogLevel.ERROR, LogLevel.DEV) }

fun main() {

    logger.dev { "Loading finish!" }
    logger.debug { "Loading finish!" }
    logger.info { "Loading finish!" }
    logger.warm { "Loading finish!" }
    logger.error { "Loading finish!" }
    println()
    logger.dev("list", listOf(1, 2, 3, 4, 5, 6, 7))
    logger.dev("list", listOf("aaa", "bbb", "ccc"))
    println()
    logger.dev("array", arrayOf(1, 2, 3, 4, 5, 6, 7))
    logger.dev("array", arrayOf("aaa", "bbb", "ccc"))
    println()
    logger.dev("array", setOf(1, 2, 3, 4, 5, 6, 7))
    logger.dev("array", setOf("aaa", "bbb", "ccc"))
    println()
    logger.dev("map", mapOf(Pair(1, "a"), Pair(2, "b"), Pair(3, "c")))
    println()
    logger.dev("Any", LoggerTest())
    logger.dev("String", "LoggerTest() +++ 123")

}