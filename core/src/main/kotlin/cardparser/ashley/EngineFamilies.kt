package cardparser.ashley

import cardparser.ashley.components.*
import com.badlogic.ashley.core.Family
import ktx.ashley.allOf
import ktx.ashley.exclude

val ALL_STACK_FAMILY: Family = allOf(TransformComp::class,
        StackComp::class).get()

/** only Bottom && Up stacks*/
val STACK_FAMILY: Family = allOf(TransformComp::class, GraphicComp::class,
        StackComp::class, DragComp::class).exclude(MainStackComp::class).get()
val MAIN_STACK_FAMILY: Family = allOf(TransformComp::class, GraphicComp::class,
        StackComp::class, MainStackComp::class, DragComp::class).get()
val CARDS_FAMILY: Family = allOf(TransformComp::class, GraphicComp::class,
        CardComp::class, TouchComp::class).get()


//@Deprecated("Wrap to new instances Stack")
//fun Engine.getOurGameStacks(): List<Stack> {
//    return this.getEntitiesFor(STACK_FAMILY).toList().map { Stack(it) }
//}
//
//@Deprecated("Wrap to new instances Card")
//fun Engine.getOurGameCards(): List<Card> {
//    return this.getEntitiesFor(CARDS_FAMILY).toList().map { Card(it) }
//}