package com.kemp.core.rendering

import com.kemp.core.interfaces.Disposable
import com.kemp.core.utils.Mat3

interface ImageBasedLighting : Disposable {
    fun intensity(value: Float)
    fun intensity(): Float
    fun rotation(mat: Mat3)
    fun rotation(): Mat3
}