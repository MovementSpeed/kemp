package com.kemp.core.rendering.ui

import com.kemp.core.input.touch.TouchElement
import com.kemp.core.input.touch.TouchStick
import com.kemp.core.utils.Color

class DefaultTouchStickRenderer : TouchElementRenderer {
    override fun render(renderer2D: Renderer2D, touchElement: TouchElement) {
        if (touchElement is TouchStick) {
            // Touch range
            renderer2D.color = Color(255, 255, 255)
            renderer2D.drawStyle = Renderer2D.DrawStyle.Stroke
            renderer2D.strokeWidth = 2f
            renderer2D.drawCircle(touchElement.x, touchElement.y, touchElement.radiusPx)

            if (touchElement.active) {
                renderer2D.color = Color(255, 0, 0)
            } else {
                renderer2D.color = Color(255, 255, 255)
            }

            // Touch stick
            renderer2D.drawStyle = Renderer2D.DrawStyle.Fill
            renderer2D.drawCircle(
                touchElement.x - touchElement.relativeStickPosition.x,
                touchElement.y - touchElement.relativeStickPosition.y,
                touchElement.radiusPx / 2f)
        }
    }
}