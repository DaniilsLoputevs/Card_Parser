package cardparser.ashley.systems.parts.screeninput

import cardparser.CARD_WIDTH
import cardparser.ashley.components.adapters.GameCardAdapter
import cardparser.gameStrucures.GameRepository
import com.badlogic.gdx.math.Vector2

class MainStackClickProcessor: ScreenInputProcessor {

    private val secondStackPos = Vector2(60.25f * 2 + CARD_WIDTH, 520f)

    private var transferCard: GameCardAdapter? = null
    private var currPosition: Vector2 = Vector2(Vector2.Zero)
//    private var currentsStack: GameStackAdapter = GameStackAdapter()

//    lateinit var gameViewport: Viewport
//    lateinit var context: GameContext
    lateinit var gameRep: GameRepository

    override fun onTouch(cursorPosition: Vector2) {
        println("ON TOUCH!!!")
    }

    override fun onDoubleTouch(cursorPosition: Vector2) {
        println("ON DOUBLE TOUCH!!!")
    }


}