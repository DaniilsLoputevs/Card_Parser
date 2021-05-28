package cardparser.ui

import cardparser.asset.FontAsset
import cardparser.asset.UIAtlasAssets
import cardparser.ui.LabelStyles.*
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.imageButton
import ktx.style.label
import ktx.style.skin

enum class LabelStyles {
    BLACK32, BLACK24,
    WHITE46, WHITE32, WHITE24,
    WHITE46_B, WHITE32_B, WHITE24_B,
    WHITE46_BI, WHITE32_BI, WHITE24_BI,
    WHITE46_I, WHITE32_I, WHITE24_I,
}

enum class ImageButtonStyle { SPADES, HEARTS, CLUBS, DIAMONDS }

enum class AnglesButtonStyle { LEFTUP, RIGHTUP, LEFTDOWN, RIGHTDOWN }

fun createSkin(assets: AssetStorage) {
    val gui = assets[UIAtlasAssets.UI.desc]
    Scene2DSkin.defaultSkin = skin(gui) { skin ->
        createLabels(assets)
        createImageButtons(skin)
    }
}

private fun Skin.createLabels(assets: AssetStorage) {
    label(BLACK32.name) {
        font = assets[FontAsset.FONT_32_BLACK.desc]
    }
    label(BLACK24.name) {
        font = assets[FontAsset.FONT_24_BLACK.desc]
    }
    label(WHITE46.name) {
        font = assets[FontAsset.FONT_46_WHITE.desc]
    }
    label(WHITE32.name) {
        font = assets[FontAsset.FONT_32_WHITE.desc]
    }
    label(WHITE24.name) {
        font = assets[FontAsset.FONT_24_WHITE.desc]
    }
    label(WHITE46_BI.name) {
        font = assets[FontAsset.FONT_46_WHITE_BOLD_ITALIC.desc]
    }
    label(WHITE32_BI.name) {
        font = assets[FontAsset.FONT_32_WHITE_BOLD_ITALIC.desc]
    }
    label(WHITE24_BI.name) {
        font = assets[FontAsset.FONT_24_WHITE_BOLD_ITALIC.desc]
    }
    label(WHITE46_B.name) {
        font = assets[FontAsset.FONT_46_WHITE_BOLD.desc]
    }
    label(WHITE32_B.name) {
        font = assets[FontAsset.FONT_32_WHITE_BOLD.desc]
    }
    label(WHITE24_B.name) {
        font = assets[FontAsset.FONT_24_WHITE_BOLD.desc]
    }
    label(WHITE46_I.name) {
        font = assets[FontAsset.FONT_46_WHITE_ITALIC.desc]
    }
    label(WHITE32_I.name) {
        font = assets[FontAsset.FONT_32_WHITE_ITALIC.desc]
    }
    label(WHITE24_I.name) {
        font = assets[FontAsset.FONT_24_WHITE_ITALIC.desc]
    }
}

private fun Skin.createImageButtons(skin: Skin) {
    ImageButtonStyle.values().forEach {
        imageButton(it.name) {
            imageUp = skin.getDrawable(it.name)
            imageDown = imageUp
        }
    }
    AnglesButtonStyle.values().forEach {
        imageButton(it.name) {
            imageUp = skin.getDrawable(it.name)
            imageDown = imageUp
        }
    }
}
