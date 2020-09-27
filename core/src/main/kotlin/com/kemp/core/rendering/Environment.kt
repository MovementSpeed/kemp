package com.kemp.core.rendering

import com.kemp.core.utils.Color

interface Environment {
    fun color(r: Float, g: Float, b: Float, a: Float)
    fun color(c: Color)
    fun intensity(): Float
}