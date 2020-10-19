package com.kemp.android.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceView

class KempView : SurfaceView {
    private val renderDelegates: MutableList<AndroidRenderDelegate> = mutableListOf()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { canv ->
            renderDelegates.forEach { it.draw(canv) }
            invalidate()
        }
    }

    fun addRenderDelegate(renderDelegate: AndroidRenderDelegate) {
        renderDelegates.add(renderDelegate)
    }
}