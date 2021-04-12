package cardparser.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

private const val GRAPHIC_DIR: String = "graphics"

enum class CardBackAtlas(
        directory: String,
        fileName: String,
        val desc: AssetDescriptor<TextureAtlas> = AssetDescriptor("$GRAPHIC_DIR/$directory/$fileName", TextureAtlas::class.java)
) {
    CARD_BACK_DEFAULT("card-back", "card_back.atlas")
}

enum class CardDeckAtlas(
        directory: String,
        fileName: String,
        val desc: AssetDescriptor<TextureAtlas> = AssetDescriptor("$GRAPHIC_DIR/$directory/$fileName", TextureAtlas::class.java)
) {
    CARD_DECK_DEFAULT("card-deck", "card_deck_default.atlas"),
}

enum class GeneralAsset(
        directory: String,
        fileName: String,
        val desc: AssetDescriptor<Texture> = AssetDescriptor("$GRAPHIC_DIR/$directory/$fileName", Texture::class.java)
) {
    BACKGROUND_DEFAULT("general", "default_background_screen.png"),
    CARD_STACK("general", "card_stack.png")
}
