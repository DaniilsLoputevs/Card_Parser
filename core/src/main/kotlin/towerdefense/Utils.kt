package towerdefense

import com.badlogic.ashley.core.*
import com.badlogic.gdx.Application.ApplicationType.Android
import com.badlogic.gdx.Application.ApplicationType.Desktop
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import ktx.ashley.get

/**
 * For using in [EntitySystem].
 *
 * Ashley guarantees to as, that came entity into System HAVE REQUIREMENT components.
 * So, this function need for Smart Cast, cause Java API, Technically provide nullable return type,
 * but in real it never will happen.
 */
fun <T : Component> Entity.findComponent(mapper: ComponentMapper<T>): T {
    val rslComponent = this[mapper]
    require(rslComponent != null) { "Entity |entity| must have a GraphicComponent. entity=$this" }
    return rslComponent
}

/**
 * Use Vertical Synchronization.
 */
fun cursorPositionToWorldUnits(screenX: Float, screenY: Float): Vector2 {
    val vSyncY = Gdx.graphics.height - screenY
    var rslX: Float = screenX
    var rslY: Float = vSyncY

    //    val currAspectRation
//        Android -> Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat()
//    }
    println("DEV")

//    val currAspectRation: Float = when (Gdx.app.type) {
//        Android -> Gdx.graphics.height.toFloat() / Gdx.graphics.width.toFloat()
//        Desktop -> Gdx.graphics.width.toFloat() / Gdx.graphics.height.toFloat()
//        else -> IDEAL_SCREEN_ASPECT_RATION
//    }
//
//    if (currAspectRation < IDEAL_SCREEN_ASPECT_RATION) { // desk - height UP
//        // v3
//        val correctHeightForCurrHeight = Gdx.graphics.height.toFloat() * IDEAL_SCREEN_ASPECT_RATION
//        val offsetY = (Gdx.graphics.height - correctHeightForCurrHeight) / 2
//        rslY = vSyncY - offsetY
//        println("Gdx.graphics.height = ${Gdx.graphics.height}")
//        println("correctWidthForCurrHeight = $correctHeightForCurrHeight")
//        println("offsetY = $offsetY")
//
//
//        // v2
////        val correctHeightForCurrHeight = Gdx.graphics.height.toFloat() * IDEAL_SCREEN_ASPECT_RATION
////        val offsetY = (Gdx.graphics.height - correctHeightForCurrHeight) / 2
////        rslY = vSyncY - offsetY
////        println("Gdx.graphics.height = ${Gdx.graphics.height}")
////        println("correctWidthForCurrHeight = $correctHeightForCurrHeight")
////        println("offsetY = $offsetY")
//    } else if (currAspectRation > IDEAL_SCREEN_ASPECT_RATION) { // desk - width UP

        // v1
//        val correctWidthForCurrHeight = Gdx.graphics.width.toFloat() / IDEAL_SCREEN_ASPECT_RATION
//        val offsetY = (Gdx.graphics.height - correctWidthForCurrHeight) / 2
//        rslY = vSyncY - offsetY
//        println("Gdx.graphics.height = ${Gdx.graphics.height}")
//        println("correctWidthForCurrHeight = $correctWidthForCurrHeight")
//        println("offsetY = $offsetY")
//    }


//    println("currAspectRation = $currAspectRation")
//    println("screenX = $screenX")
//    println("screenY = $screenY")
//    println("rslX = $rslX")
//    println("rslY = $rslY")



    val wuX = (rslX / Gdx.graphics.width) * V_WORLD_WIDTH_UNITS
    val wuY = (rslY / Gdx.graphics.height) * V_WORLD_HEIGHT_UNITS
    println("wuX = $wuX")
    println("wuY = $wuY")

    println("DEV")
    return Vector2(wuX, wuY)
}

//fun Any?.ifNotNull(f: ()-> Unit){
//    if (this != null){
//        f()
//    }
//}

