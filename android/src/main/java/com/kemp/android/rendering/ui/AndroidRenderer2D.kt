package com.kemp.android.rendering.ui

import android.graphics.Canvas
import android.graphics.Paint
import com.kemp.android.ui.AndroidRenderDelegate
import com.kemp.core.rendering.ui.Renderer2D
import com.kemp.core.utils.Color

class AndroidRenderer2D : Renderer2D, AndroidRenderDelegate {
    private var canvas: Canvas? = null
    private val drawCommands = mutableListOf<() -> Unit>()

    override var color: Color = Color(255, 255, 255)
        set(value) {
            field = value

            drawCommands.add {
                paint.color = android.graphics.Color.argb(
                    value.a.toInt(),
                    value.r.toInt(),
                    value.g.toInt(),
                    value.b.toInt()
                )
            }
        }

    override var drawStyle: Renderer2D.DrawStyle = Renderer2D.DrawStyle.Fill
        set(value) {
            field = value

            drawCommands.add {
                paint.style = when (value) {
                    Renderer2D.DrawStyle.Fill -> Paint.Style.FILL
                    Renderer2D.DrawStyle.Stroke -> Paint.Style.STROKE
                    Renderer2D.DrawStyle.FillAndStroke -> Paint.Style.FILL_AND_STROKE
                }
            }
        }

    override var strokeWidth: Float = 0.0f
        set(value) {
            field = value
            drawCommands.add { paint.strokeWidth = value }
        }

    private val paint = Paint().also {
        it.isAntiAlias = true
    }

    override fun drawRect(left: Float, top: Float, right: Float, bottom: Float) {
        drawCommands.add { canvas?.drawRect(left, top, right, bottom, paint) }
    }

    override fun drawCircle(cx: Float, cy: Float, radius: Float) {
        drawCommands.add { canvas?.drawCircle(cx, cy, radius, paint) }
    }

    override fun draw(canvas: Canvas): Boolean {
        this.canvas = canvas

        for (drawCommand in drawCommands) {
            drawCommand()
        }

        drawCommands.clear()
        return true
    }
}