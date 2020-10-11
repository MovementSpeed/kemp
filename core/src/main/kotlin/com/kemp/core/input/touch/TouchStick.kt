package com.kemp.core.input.touch

import com.kemp.core.normalize
import com.kemp.core.utils.*
import kotlin.math.sign

class TouchStick(val x: Float, val y: Float, val radiusPx: Float) {
    var enabled = true
    var relativeStickPosition = Float2()
    val stickVector = Float2()

    private var pressed = false
    private var state = STICK_TOUCH_FAILED

    fun touchAt(pressed: Boolean, x: Float, y: Float) {
        val distanceX = this.x - x
        val distanceY = this.y - y

        val f2 = Pool.useFloat2()
        f2.x = distanceX
        f2.y = distanceY

        val touchInside = length(f2) <= radiusPx

        val previousPressed = this.pressed
        this.pressed = pressed

        if (!previousPressed && this.pressed && touchInside) {
            state = STICK_TOUCH_STARTED
        } else if (!previousPressed && this.pressed && !touchInside) {
            state = STICK_TOUCH_FAILED
            return
        } else if (previousPressed && this.pressed && state == 0) {
            state = STICK_TOUCH_UPDATING
        } else if (previousPressed && !this.pressed && state == 2) {
            state = STICK_TOUCH_ENDED
        }

        if (state == 1 || state == 3) {
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

    private companion object {
        const val STICK_TOUCH_STARTED = 0
        const val STICK_TOUCH_FAILED = 1
        const val STICK_TOUCH_UPDATING = 2
        const val STICK_TOUCH_ENDED = 3
    }
}