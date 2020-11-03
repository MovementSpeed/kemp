package com.kemp.android.rendering.effects

import com.google.android.filament.Engine
import com.google.android.filament.IndirectLight
import com.kemp.core.utils.Float4
import com.kemp.core.utils.Mat4
import com.kemp.core.rendering.effects.ImageBasedLighting
import com.kemp.core.utils.Float3
import com.kemp.core.utils.Mat3
import com.kemp.core.utils.rotation

class AndroidImageBasedLighting(private val engine: Engine, val indirectLight: IndirectLight) : ImageBasedLighting {
    override fun intensity(value: Float) {
        indirectLight.intensity = value
    }

    override fun intensity(): Float {
        return indirectLight.intensity
    }

    override fun rotate(eulerAngles: Float3) {
        var matrixArray = indirectLight.getRotation(null)

        val rotationMatrix = (rotation(eulerAngles) * Mat4(
            Float4(matrixArray[0], matrixArray[1], matrixArray[2], 0f),
            Float4(matrixArray[3], matrixArray[4], matrixArray[5], 0f),
            Float4(matrixArray[6], matrixArray[7], matrixArray[8], 0f),
            Float4(0f, 0f, 0f, 1f)
        )).toFloatArray()

        matrixArray = floatArrayOf(
            rotationMatrix[0], rotationMatrix[1], rotationMatrix[2],
            rotationMatrix[4], rotationMatrix[5], rotationMatrix[6],
            rotationMatrix[8], rotationMatrix[9], rotationMatrix[10]
        )

        indirectLight.setRotation(matrixArray)
    }

    override fun rotation(): Mat3 {
        val arr = indirectLight.getRotation(null)
        return Mat3.of(*arr)
    }

    override fun dispose() {
        engine.destroyIndirectLight(indirectLight)
    }
}