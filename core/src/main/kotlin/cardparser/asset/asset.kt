package cardparser.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas

private const val GRAPHIC_DIR: String = "graphics"
private const val UI_DIR: String = "ui"
private const val FONT_DIR: String = "fonts"

enum class CardBackAtlas(
        directory: String,
        fileName: String,
        val desc: AssetDescriptor<TextureAtlas> = AssetDescriptor("$GRAPHIC_DIR/$directory/$fileName", TextureAtlas::class.java)
) {
    CARD_BACK_DEFAULT("card-back", "card_back.atlas"),
    CARD_BACK_RED_DEFAULT("card-back", "back_red.atlas")
}

enum class CardDeckAtlas(
        directory: String,
        fileName: String,
        val desc: AssetDescriptor<TextureAtlas> = AssetDescriptor("$GRAPHIC_DIR/$directory/$fileName", TextureAtlas::class.java)
) {
    CARD_DECK_DEFAULT("card-deck", "card_deck_default.atlas"),
    CARD_DECK_USSR("card-deck", "card_deck_ussr.atlas"),
}

enum class GeneralAsset(
        directory: String,
        fileName: String,
        val desc: AssetDescriptor<Texture> = AssetDescriptor("$GRAPHIC_DIR/$directory/$fileName", Texture::class.java)
) {
    BACKGROUND_DEFAULT("general", "default_background_screen.png"),
    CARD_STACK("general", "card_stack.png")
}

enum class UIAtlasAssets(
    fileName: String,
    directory: String = UI_DIR,
    val desc: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName", TextureAtlas::class.java)
) {
    UI("ui.atlas")
}

enum class FontAsset(
    fileName: String,
    directory: String = FONT_DIR,
    val desc: AssetDescriptor<BitmapFont> =
        AssetDescriptor("$directory/$fileName",
            BitmapFont::class.java,
        BitmapFontLoader.BitmapFontParameter().apply {
            atlasName = UIAtlasAssets.UI.desc.fileName
        })
) {
    FONT_LARGE_BLACK("Purisa_32_black.fnt"),
    FONT_LARGE_WHITE("Purisa_32_white.fnt"),
    FONT_MEDIUM_BLACK("Purisa_24_black.fnt"),
    FONT_MEDIUM_WHITE("Purisa_24_white.fnt");
}
