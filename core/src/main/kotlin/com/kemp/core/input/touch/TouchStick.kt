package com.kemp.core.input.touch

import com.kemp.core.normalize
import com.kemp.core.utils.*

class TouchStick(val x: Float, val y: Float, val radiusPx: Float) {
    var relativeStickPosition = Float2()

    fun touchAt(x: Float, y: Float) {
        val distanceX = this.x - x
        val distanceY = this.y - y
        val distanceFromStickCenterPx = distance(this.x, this.y, x, y)

        relativeStickPosition.x = distanceX
        relativeStickPosition.y = distanceY

        val value = clamp(distanceFromStickCenterPx, 0f, radiusPx)
        relativeStickPosition = relativeStickPosition.normalize() * value
    }
}