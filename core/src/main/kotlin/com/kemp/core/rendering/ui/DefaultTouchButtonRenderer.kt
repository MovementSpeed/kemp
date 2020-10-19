package com.kemp.core.rendering.ui

import com.kemp.core.input.touch.TouchButton
import com.kemp.core.input.touch.TouchElement
import com.kemp.core.utils.Color

class DefaultTouchButtonRenderer : TouchElementRenderer {
    override fun render(renderer2D: Renderer2D, touchElement: TouchElement) {
        if (touchElement is TouchButton) {
            if (touchElement.active) {
                renderer2D.color = Color(255, 0, 0)
            } else {
                renderer2D.color = Color(255, 255, 255)
            }

            renderer2D.drawStyle = Renderer2D.DrawStyle.Fill
            renderer2D.drawRect(
                touchElement.x,
                touchElement.y,
                touchElement.x + touchElement.width,
                touchElement.y + touchElement.height)
        }
    }
}