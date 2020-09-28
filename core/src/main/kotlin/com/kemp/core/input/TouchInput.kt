package com.kemp.core.input

import com.kemp.core.utils.Float2

interface TouchInput {
    fun pointer(index: Int, x: Float, y: Float, state: TouchPointerState)
    fun pointer(index: Int): Float2
    fun pointerVelocity(index: Int): Float2
}