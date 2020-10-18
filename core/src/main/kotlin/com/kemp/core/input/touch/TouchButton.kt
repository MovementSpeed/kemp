package com.kemp.core.input.touch

import com.kemp.core.utils.Rect

class TouchButton(val x: Float, val y: Float, val width: Float, val height: Float) : TouchElement() {
    private val rect = Rect(x, y, width, height)

    override fun touchInside(x: Float, y: Float): Boolean {
        return rect.pointInRect(x, y)
    }
}