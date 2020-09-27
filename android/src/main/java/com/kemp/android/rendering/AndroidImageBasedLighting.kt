package com.kemp.android.rendering

import com.google.android.filament.IndirectLight
import com.kemp.core.rendering.ImageBasedLighting
import com.kemp.core.utils.Mat3

class AndroidImageBasedLighting(val indirectLight: IndirectLight) : ImageBasedLighting {
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
}