package com.kemp.core.models

import com.kemp.core.*

interface MaterialInstance {
    val name: String

    fun setParameter(name: String, value: Boolean)
    fun setParameter(name: String, value: Float)
    fun setParameter(name: String, value: Int)

    fun setParameter(name: String, value1: Boolean, value2: Boolean)
    fun setParameter(name: String, value1: Float, value2: Float)
    fun setParameter(name: String, value1: Int, value2: Int)

    fun setParameter(name: String, value1: Boolean, value2: Boolean, value3: Boolean)
    fun setParameter(name: String, value1: Float, value2: Float, value3: Float)
    fun setParameter(name: String, value1: Int, value2: Int, value3: Int)

    fun setParameter(name: String, value1: Boolean, value2: Boolean, value3: Boolean, value4: Boolean)
    fun setParameter(name: String, value1: Float, value2: Float, value3: Float, value4: Float)
    fun setParameter(name: String, value1: Int, value2: Int, value3: Int, value4: Int)

    fun setParameter(name: String, texture: Texture, sampler: TextureSampler)
    fun setParameter(name: String, type: BooleanElement, v: BooleanArray, offset: Int, count: Int)
    fun setParameter(name: String, type: IntElement, v: IntArray, offset: Int, count: Int)
    fun setParameter(name: String, type: RgbType, r: Float, g: Float, b: Float)
    fun setParameter(name: String, type: RgbaType, r: Float, g: Float, b: Float, a: Float)
    fun setScissor(left: Int, bottom: Int, width: Int, height: Int)
    fun unsetScissor()
    fun setPolygonOffset(scale: Float, constant: Float)
    fun setMaskThreshold(threshold: Float)
    fun setSpecularAntiAliasingVariance(variance: Float)
    fun setSpecularAntiAliasingThreshold(threshold: Float)
    fun setDoubleSided(doubleSided: Boolean)
    fun setCullingMode(mode: CullingMode)
}