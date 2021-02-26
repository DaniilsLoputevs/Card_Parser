package lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import towerdefense.MainGame

/**
 * TODO - найти иконку:  setWindowIcon("icon.png")
 *
 * Kotlin: main method  like in java
 */
fun main() {
    Lwjgl3Application(
        MainGame(), // create game
        Lwjgl3ApplicationConfiguration().apply {
            setTitle("tower_defense")
            setWindowedMode(640, 480)
            setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        })
}
