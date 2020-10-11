package com.kemp.core.input.touch

import com.kemp.core.normalize
import com.kemp.core.utils.*

class TouchStick(val x: Float, val y: Float, val radiusPx: Float) {
    var relativeStickPosition = Float2()
    var pressedState = false

    fun touchAt(pressed: Boolean, x: Float, y: Float) {
        if (!pressed) {
            pressedState = false
            relativeStickPosition.x = 0f
            relativeStickPosition.y = 0f
            return
        }

        val distanceX = this.x - x
        val distanceY = this.y - y
        val distanceFromTouch = distance(this.x, this.y, x, y)

        val f2 = Pool.useFloat2()
        f2.x = distanceX
        f2.y = distanceY

        if (length(f2) <= radiusPx || pressedState) {
            pressedState = true
            relativeStickPosition.x = distanceX
            relativeStickPosition.y = distanceY

            val value = clamp(distanceFromTouch, 0f, radiusPx)
            relativeStickPosition = relativeStickPosition.normalize() * value
        }
    }
}