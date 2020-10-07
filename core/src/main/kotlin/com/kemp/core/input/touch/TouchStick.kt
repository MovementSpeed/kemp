package com.kemp.core.input.touch

import com.kemp.core.utils.distance
import com.kemp.core.utils.normalize

class TouchStick(val x: Float, val y: Float, val radiusPx: Float) {
    var power = 0f

    fun touchAt(x: Float, y: Float) {
        val distanceFromStickCenterPx = distance(this.x, this.y, x, y)
        if (distanceFromStickCenterPx <= radiusPx) {
            power = normalize(distanceFromStickCenterPx, 0f, radiusPx)
        }
    }
}