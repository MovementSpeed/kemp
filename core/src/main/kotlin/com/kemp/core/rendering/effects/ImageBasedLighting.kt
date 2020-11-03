package com.kemp.core.rendering.effects

import com.kemp.core.interfaces.Disposable
import com.kemp.core.utils.Float3
import com.kemp.core.utils.Mat3

interface ImageBasedLighting : Disposable {
    fun intensity(value: Float)
    fun intensity(): Float
    fun rotate(eulerAngles: Float3)
    fun rotation(): Mat3
}