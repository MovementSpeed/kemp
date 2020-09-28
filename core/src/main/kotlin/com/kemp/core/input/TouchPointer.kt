package com.kemp.core.input

import com.kemp.core.utils.Float2
import com.kemp.core.utils.Pool

data class TouchPointer(
    val index: Int,
    var x: Float, var y: Float,
    var oldX: Float, var oldY: Float,
    var state: TouchPointerState
) {
    val velocity: Float2
        get() {
            val v = Pool.useFloat2()
            v.x = x - oldX
            v.y = y - oldY
            return v
        }
}