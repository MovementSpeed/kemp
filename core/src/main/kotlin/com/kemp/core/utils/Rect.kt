package com.kemp.core.utils

data class Rect(val x: Float, val y: Float, val width: Float, val height: Float) {
    fun pointInRect(x: Float, y: Float): Boolean {
        return x in this.x..(this.x + width) && y in this.y..(this.y + height)
    }
}