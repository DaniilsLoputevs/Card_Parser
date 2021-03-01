package towerdefense.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

private const val GRAPHIC_DIR : String = "graphics"

//enum class SoundAsset(
//    fileName: String,
//    directory: String = "sound",
//    val descriptor: AssetDescriptor<Sound> = AssetDescriptor("$directory/$fileName", Sound::class.java)
//) {
//    BOOST_1("boost1.wav"),
//    BOOST_2("boost2.wav"),
//    EXPLOSION("explosion.wav"),
//    LIFE("life.wav"),
//    SHIELD("shield.wav"),
//    DAMAGE("damage.wav"),
//    BLOCK("block.wav"),
//    SPAWN("spawn.wav")
//}

//enum class MusicAsset(
//    fileName: String,
//    directory: String = "music",
//    val descriptor: AssetDescriptor<Music> = AssetDescriptor("$directory/$fileName", Music::class.java)
//) {
//    GAME("game.mp3"),
//    GAME_OVER("gameOver.mp3"),
//    MENU("menu.mp3")
//}

enum class TextureAtlasAsset(
    val isSkinAtlas: Boolean,
    fileName: String,
    directory: String = "graphics",
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName", TextureAtlas::class.java)
) {
//    GRAPHICS(false, "graphics.atlas"),
//    UI(true, "ui.atlas", "ui"),
//    MY_FIRST_ATLAS(false, "test.atlas", "graphics/card-back"),
//    BACKGROUND(false, "background-screen.atlas")
    TEST_CARD_BACK(false, "test-2.atlas", "$GRAPHIC_DIR/card-back")
}

enum class TextureAsset(
    fileName: String,
    directory: String,
    val descriptor: AssetDescriptor<Texture> = AssetDescriptor("$directory/$fileName", Texture::class.java)
) {
    DEFAULT_BACKGROUND("default-background-screen.png", "$GRAPHIC_DIR/background")
}

//enum class ShaderProgramAsset(
//    vertexFileName: String,
//    fragmentFileName: String,
//    directory: String = "shader",
//    val descriptor: AssetDescriptor<ShaderProgram> = AssetDescriptor(
//        "$directory/$vertexFileName/$fragmentFileName",
//        ShaderProgram::class.java,
//        ShaderProgramLoader.ShaderProgramParameter().apply {
//            vertexFile = "$directory/$vertexFileName"
//            fragmentFile = "$directory/$fragmentFileName"
//        })
//) {
//    OUTLINE("default.vert", "outline.frag")
//}

//enum class BitmapFontAsset(
//    fileName: String,
//    directory: String = "ui",
//    val descriptor: AssetDescriptor<BitmapFont> = AssetDescriptor(
//        "$directory/$fileName",
//        BitmapFont::class.java,
//        BitmapFontLoader.BitmapFontParameter().apply {
//            atlasName = TextureAtlasAsset.UI.descriptor.fileName
//        })
//) {
//    FONT_LARGE_GRADIENT("font11_gradient.fnt"),
//    FONT_DEFAULT("font8.fnt")
//}

//enum class I18NBundleAsset(
//    fileName: String,
//    directory: String = "i18n",
//    val descriptor: AssetDescriptor<I18NBundle> = AssetDescriptor("$directory/$fileName", I18NBundle::class.java)
//) {
//    DEFAULT("i18n")
//}
