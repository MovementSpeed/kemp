package com.kemp.core.input

import com.kemp.core.utils.Float2

interface TouchInput {
    fun update()

    fun pointerX(index: Int): Float
    fun pointerY(index: Int): Float
    fun pointer(index: Int): Float2

    fun pointerDeltaX(index: Int): Float
    fun pointerDeltaY(index: Int): Float
    fun pointerDelta(index: Int): Float2
}