package cardparser.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

fun isPress(): Boolean = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
fun isNotPress(): Boolean = !Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
fun isContinuePress(): Boolean = Gdx.input.isButtonPressed(Input.Buttons.LEFT)
fun isNotContinuePress(): Boolean = !Gdx.input.isButtonPressed(Input.Buttons.LEFT)
