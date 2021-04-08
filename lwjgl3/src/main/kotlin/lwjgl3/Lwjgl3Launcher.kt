package lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import towerdefense.MainGame

/**
 * aspect ration: 16:9 (width X height)
 *
 * 2160p: 3840 x 2160
 * 1440p: 2560 x 1440
 * 1080p: 1920 x 1080
 * 720p: 1280 x 720
 * 480p: 854 x 480
 * 360p: 640 x 360
 * 240p: 426 x 240
 */
fun main() {
    Lwjgl3Application(
            MainGame(), // create game
            Lwjgl3ApplicationConfiguration().apply {
                setTitle("Card Parser by Daniils & Maksim. ))))")
                setWindowSizeLimits(426, 240, -1, -1)
                setWindowedMode(854, 480) // 16 : 9
                setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
            })
}

//            setWindowedMode(1280, 720) // 16 : 9
//            setWindowedMode(1280 ,600)
//            setWindowedMode(640, 480)
//            setWindowedMode(360, 640) // 16 : 9
