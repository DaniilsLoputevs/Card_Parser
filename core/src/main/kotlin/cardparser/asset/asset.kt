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
    FONT_46_WHITE("Purisa_46_white.fnt"),
    FONT_46_WHITE_BOLD("Purisa_46_white_bold.fnt"),
    FONT_46_WHITE_ITALIC("Purisa_46_white_italic.fnt"),
    FONT_46_WHITE_BOLD_ITALIC("Purisa_46_white_bold_italic.fnt"),
    FONT_32_BLACK("Purisa_32_black.fnt"),
    FONT_32_WHITE("Purisa_32_white.fnt"),
    FONT_32_WHITE_BOLD("Purisa_32_white_bold.fnt"),
    FONT_32_WHITE_ITALIC("Purisa_32_white_italic.fnt"),
    FONT_32_WHITE_BOLD_ITALIC("Purisa_32_white_bold_italic.fnt"),
    FONT_24_BLACK("Purisa_24_black.fnt"),
    FONT_24_WHITE("Purisa_24_white.fnt"),
    FONT_24_WHITE_BOLD("Purisa_24_white_bold.fnt"),
    FONT_24_WHITE_ITALIC("Purisa_24_white_italic.fnt"),
    FONT_24_WHITE_BOLD_ITALIC("Purisa_24_white_bold_italic.fnt"),
}
