package cardparser.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class GraphicComp : Component, Pool.Poolable {
    val sprite = Sprite()

    override fun reset() {
        sprite.texture = null
        sprite.setColor(1f, 1f, 1f, 1f)
    }

    fun setSpriteRegion(region: TextureRegion) {
        sprite.run {
            setRegion(region)
            setSize(region.regionWidth.toFloat(), region.regionHeight.toFloat())
        }
    }

    fun setSpriteRegion(region: Texture) {
        sprite.run {
            setRegion(region)
            setSize(region.width.toFloat(), region.height.toFloat())
        }
        this.javaClass::getSimpleName
    }

    override fun toString(): String = "GraphicComponent"

    companion object {
        val mapper = mapperFor<GraphicComp>()
    }
}
