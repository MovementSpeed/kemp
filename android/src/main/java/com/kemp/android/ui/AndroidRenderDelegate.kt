package com.kemp.android.ui

import android.graphics.Canvas

interface AndroidRenderDelegate {
    fun draw(canvas: Canvas): Boolean
}