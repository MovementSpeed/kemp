package com.kemp.core.utils

class Box {
    /**
     * Returns the center of the 3D box.
     *
     * @return an XYZ float array of size 3
     */
    val center = FloatArray(3)

    /**
     * Returns the half-extent from the center of the 3D box.
     *
     * @return an XYZ float array of size 3
     */
    val halfExtent = FloatArray(3)

    /**
     * Default-initializes the 3D box to have a center and half-extent of (0,0,0).
     */
    constructor() {}

    /**
     * Initializes the 3D box from its center and half-extent.
     */
    constructor(
        centerX: Float, centerY: Float, centerZ: Float,
        halfExtentX: Float, halfExtentY: Float, halfExtentZ: Float
    ) {
        center[0] = centerX
        center[1] = centerY
        center[2] = centerZ
        halfExtent[0] = halfExtentX
        halfExtent[1] = halfExtentY
        halfExtent[2] = halfExtentZ
    }

    /**
     * Initializes the 3D box from its center and half-extent.
     *
     * @param center     a float array with XYZ coordinates representing the center of the box
     * @param halfExtent a float array with XYZ coordinates representing half the size of the box in
     * each dimension
     */
    constructor(center: FloatArray, halfExtent: FloatArray) {
        center[0] = center[0]
        center[1] = center[1]
        center[2] = center[2]
        halfExtent[0] = halfExtent[0]
        halfExtent[1] = halfExtent[1]
        halfExtent[2] = halfExtent[2]
    }

    /**
     * Sets the center of of the 3D box.
     */
    fun setCenter(centerX: Float, centerY: Float, centerZ: Float) {
        center[0] = centerX
        center[1] = centerY
        center[2] = centerZ
    }

    /**
     * Sets the half-extent of the 3D box.
     */
    fun setHalfExtent(halfExtentX: Float, halfExtentY: Float, halfExtentZ: Float) {
        halfExtent[0] = halfExtentX
        halfExtent[1] = halfExtentY
        halfExtent[2] = halfExtentZ
    }
}