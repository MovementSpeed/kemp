package com.kemp.android.rendering.effects

import com.google.android.filament.Engine
import com.google.android.filament.IndirectLight
import com.kemp.core.rendering.effects.ImageBasedLighting
import com.kemp.core.utils.Mat3

class AndroidImageBasedLighting(private val engine: Engine, val indirectLight: IndirectLight) : ImageBasedLighting {
    override fun intensity(value: Float) {
        indirectLight.intensity = value
    }

    override fun intensity(): Float {
        return indirectLight.intensity
    }

    override fun rotation(mat: Mat3) {
        indirectLight.setRotation(mat.toFloatArray())
    }

    override fun rotation(): Mat3 {
        val arr = indirectLight.getRotation(null)
        return Mat3.of(*arr)
    }

    override fun dispose() {
        engine.destroyIndirectLight(indirectLight)
    }
}