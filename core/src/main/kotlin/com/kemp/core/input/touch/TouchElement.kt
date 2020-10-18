package com.kemp.core.input.touch

abstract class TouchElement {
    var enabled: Boolean = true
    var active: Boolean = false

    protected var state = TOUCH_FAILED
    private var pressed = false

    abstract fun touchInside(x: Float, y: Float): Boolean
    open fun touchAt(pressed: Boolean, x: Float, y: Float) {
        computeState(pressed, x, y)
    }

    protected open fun computeState(pressed: Boolean, x: Float, y: Float) {
        val touchInside = touchInside(x, y)

        val previousPressed = this.pressed
        this.pressed = pressed

        if (!previousPressed && this.pressed && touchInside) {
            state = TOUCH_STARTED
        } else if (!previousPressed && this.pressed && !touchInside) {
            state = TOUCH_FAILED
            return
        } else if (previousPressed && this.pressed && state == 0) {
            state = TOUCH_UPDATING
        } else if (previousPressed && !this.pressed && state == 2) {
            state = TOUCH_ENDED
        }

        active = state == TOUCH_UPDATING
    }

    companion object {
        const val TOUCH_STARTED = 0
        const val TOUCH_FAILED = 1
        const val TOUCH_UPDATING = 2
        const val TOUCH_ENDED = 3
    }
}