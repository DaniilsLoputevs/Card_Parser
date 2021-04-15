package cardparser.gameStrucures

import cardparser.ashley.components.adapters.GameCardAdapter

/**
 * Experiments.
 */
fun main() {
    val sys = TriggerSys().apply {
//        it.triggers = List()
    }
}

class TriggerSys {
//    lateinit var triggers

//    fun update() = triggers.forEach { it.update() }

}

abstract class Trigger(
        private val expression: () -> Boolean,
        private val script: () -> Unit
) {
    fun update() = if (expression.invoke()) script.invoke() else Unit

}

class ScriptBundle {
    lateinit var capturedCard : GameCardAdapter


//    fun capture()
}