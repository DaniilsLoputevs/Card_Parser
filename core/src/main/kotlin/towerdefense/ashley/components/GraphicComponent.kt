package towerdefense.ashley.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import towerdefense.UNIT_SCALE

class GraphicComponent : Component, Pool.Poolable {
    val sprite = Sprite()

    override fun reset() {
        sprite.texture = null
        sprite.setColor(1f, 1f, 1f, 1f)
    }

    fun setSpriteRegion(region: TextureRegion) {

        println("DEV")
        println("region.regionWidth ${region.regionWidth}")
        println("region.regionHeight ${region.regionHeight}")
        println("region.regionWidth unit ${region.regionWidth * UNIT_SCALE}")
        println("region.regionHeight unit ${region.regionHeight * UNIT_SCALE}")
        println("DEV")
        sprite.run {
            setRegion(region)
            setSize(region.regionWidth * UNIT_SCALE, region.regionHeight * UNIT_SCALE)
            setOriginCenter()
        }
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}
