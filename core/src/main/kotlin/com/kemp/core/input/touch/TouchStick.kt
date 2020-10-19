package com.kemp.core.input.touch

import com.kemp.core.normalize
import com.kemp.core.rendering.ui.Renderer2D
import com.kemp.core.utils.*

class TouchStick(val x: Float, val y: Float, val radiusPx: Float) : TouchElement() {
    var relativeStickPosition = Float2()
    val stickVector = Float2()

    override fun touchInside(x: Float, y: Float): Boolean {
        val distanceX = this.x - x
        val distanceY = this.y - y

        val f2 = Pool.useFloat2()
        f2.x = distanceX
        f2.y = distanceY

        return length(f2) <= radiusPx
    }

    override fun touchAt(pressed: Boolean, x: Float, y: Float) {
        super.touchAt(pressed, x, y)

        val distanceX = this.x - x
        val distanceY = this.y - y

        if (state == TOUCH_FAILED || state == TOUCH_ENDED) {
            relativeStickPosition.x = 0f
            relativeStickPosition.y = 0f
            stickVector.x = 0f
            stickVector.y = 0f
        } else {
            val distanceFromTouch = distance(this.x, this.y, x, y)
            relativeStickPosition.x = distanceX
            relativeStickPosition.y = distanceY

            val power = clamp(distanceFromTouch, 0f, radiusPx)

            val norm = relativeStickPosition.normalize()
            relativeStickPosition = norm * power

            stickVector.x = relativeStickPosition.x / radiusPx
            stickVector.y = relativeStickPosition.y / radiusPx
        }
    }
}