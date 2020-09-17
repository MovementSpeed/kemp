package com.kemp.android.models

import com.kemp.android.*
import com.kemp.core.*
import com.kemp.core.models.MaterialInstance

class AndroidMaterialInstance(private val materialInstance: FilamentMaterialInstance) : MaterialInstance {
    override val name: String
        get() = materialInstance.name

    override fun setParameter(name: String, value: Boolean) {
        materialInstance.setParameter(name, value)
    }

    override fun setParameter(name: String, value: Float) {
        materialInstance.setParameter(name, value)
    }

    override fun setParameter(name: String, value: Int) {
        materialInstance.setParameter(name, value)
    }

    override fun setParameter(name: String, value1: Boolean, value2: Boolean) {
        materialInstance.setParameter(name, value1, value2)
    }

    override fun setParameter(name: String, value1: Float, value2: Float) {
        materialInstance.setParameter(name, value1, value2)
    }

    override fun setParameter(name: String, value1: Int, value2: Int) {
        materialInstance.setParameter(name, value1, value2)
    }

    override fun setParameter(name: String, value1: Boolean, value2: Boolean, value3: Boolean) {
        materialInstance.setParameter(name, value1, value2, value3)
    }

    override fun setParameter(name: String, value1: Float, value2: Float, value3: Float) {
        materialInstance.setParameter(name, value1, value2, value3)
    }

    override fun setParameter(name: String, value1: Int, value2: Int, value3: Int) {
        materialInstance.setParameter(name, value1, value2, value3)
    }

    override fun setParameter(name: String, value1: Boolean, value2: Boolean, value3: Boolean, value4: Boolean) {
        materialInstance.setParameter(name, value1, value2, value3, value4)
    }

    override fun setParameter(name: String, value1: Float, value2: Float, value3: Float, value4: Float) {
        materialInstance.setParameter(name, value1, value2, value3, value4)
    }

    override fun setParameter(name: String, value1: Int, value2: Int, value3: Int, value4: Int) {
        materialInstance.setParameter(name, value1, value2, value3, value4)
    }

    override fun setParameter(name: String, texture: Texture, sampler: TextureSampler) {
        materialInstance.setParameter(name, texture as FilamentTexture, sampler as FilamentTextureSampler)
    }

    override fun setParameter(name: String, type: BooleanElement, v: BooleanArray, offset: Int, count: Int) {
        materialInstance.setParameter(name, type as FilamentBooleanElement, v, offset, count)
    }

    override fun setParameter(name: String, type: IntElement, v: IntArray, offset: Int, count: Int) {
        materialInstance.setParameter(name, type as FilamentIntElement, v, offset, count)
    }

    override fun setParameter(name: String, type: RgbType, r: Float, g: Float, b: Float) {
        materialInstance.setParameter(name, type as FilamentRgbType, r, g, b)
    }

    override fun setParameter(name: String, type: RgbaType, r: Float, g: Float, b: Float, a: Float) {
        materialInstance.setParameter(name, type as FilamentRgbaType, r, g, b, a)
    }

    override fun setScissor(left: Int, bottom: Int, width: Int, height: Int) {
        materialInstance.setScissor(left, bottom, width, height)
    }

    override fun unsetScissor() {
        materialInstance.unsetScissor()
    }

    override fun setPolygonOffset(scale: Float, constant: Float) {
        materialInstance.setPolygonOffset(scale, constant)
    }

    override fun setMaskThreshold(threshold: Float) {
        materialInstance.setMaskThreshold(threshold)
    }

    override fun setSpecularAntiAliasingVariance(variance: Float) {
        materialInstance.setSpecularAntiAliasingVariance(variance)
    }

    override fun setSpecularAntiAliasingThreshold(threshold: Float) {
        materialInstance.setSpecularAntiAliasingThreshold(threshold)
    }

    override fun setDoubleSided(doubleSided: Boolean) {
        materialInstance.setDoubleSided(doubleSided)
    }

    override fun setCullingMode(mode: CullingMode) {
        materialInstance.setCullingMode(mode as FilamentCullingMode)
    }
}