package cardparser.utils

import cardparser.asset.FontAsset
import cardparser.asset.UIAtlasAssets
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin
import cardparser.utils.LabelStyles.*

enum class LabelStyles { BLACK32, BLACK24, WHITE32, WHITE24; }

fun createSkin(assets: AssetStorage) {
    val uiFontSkins = assets[UIAtlasAssets.FONTS.desc]
    val black32 = assets[FontAsset.FONT_LARGE_BLACK.desc]
    val black24 = assets[FontAsset.FONT_MEDIUM_BLACK.desc]
    val white32 = assets[FontAsset.FONT_LARGE_WHITE.desc]
    val white24 = assets[FontAsset.FONT_MEDIUM_WHITE.desc]
    Scene2DSkin.defaultSkin = skin(uiFontSkins) {
        label(BLACK32.name) {
            font = black32
        }
        label(BLACK24.name) {
            font = black24
        }
        label(WHITE32.name) {
            font = white32
        }
        label(WHITE24.name) {
            font = white24
        }
    }
}
