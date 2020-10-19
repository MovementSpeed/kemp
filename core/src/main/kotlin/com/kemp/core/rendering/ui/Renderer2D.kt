package com.kemp.core.rendering.ui

import com.kemp.core.utils.Color

interface Renderer2D {
    var color: Color
    var drawStyle: DrawStyle
    var strokeWidth: Float

    fun drawRect(left: Float, top: Float, right: Float, bottom: Float)
    fun drawCircle(cx: Float, cy: Float, radius: Float)

    enum class DrawStyle {
        Fill, Stroke, FillAndStroke
    }
}