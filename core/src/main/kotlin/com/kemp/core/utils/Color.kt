package com.kemp.core.utils

data class Color(var r: Short, var g: Short, var b: Short, var a: Short = 255) {
    val array: FloatArray
        get() = floatArrayOf(r * 0.00392156862f, g * 0.00392156862f, b * 0.00392156862f, a * 0.00392156862f)
}