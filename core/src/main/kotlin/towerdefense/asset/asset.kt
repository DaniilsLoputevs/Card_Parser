package towerdefense.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

private const val GRAPHIC_DIR: String = "graphics"


enum class CardDeckAtlas(
        directory: String,
        fileName: String,
        val desc: AssetDescriptor<TextureAtlas> = AssetDescriptor("$GRAPHIC_DIR/$directory/$fileName", TextureAtlas::class.java)
) {
    CARD_DECK_DEFAULT("card-deck", "first-card-deck.atlas")
}

enum class CardBackAtlas(
        directory: String,
        fileName: String,
        val desc: AssetDescriptor<TextureAtlas> = AssetDescriptor("$GRAPHIC_DIR/$directory/$fileName", TextureAtlas::class.java)
) {
    CARD_BACK_DEFAULT("card-back", "test-2.atlas")
}

enum class ImgAsset(
        directory: String,
        fileName: String,
        val desc: AssetDescriptor<Texture> = AssetDescriptor("$GRAPHIC_DIR/$directory/$fileName", Texture::class.java)
) {
    BACKGROUND_DEFAULT("background", "default-background-screen.png")
}
