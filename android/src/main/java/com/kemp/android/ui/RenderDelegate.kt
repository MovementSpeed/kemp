package com.kemp.android.ui

import android.graphics.Canvas

interface RenderDelegate {
    fun draw(canvas: Canvas): Boolean
}