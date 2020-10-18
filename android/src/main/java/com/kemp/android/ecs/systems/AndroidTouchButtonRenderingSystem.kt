package com.kemp.android.ecs.systems

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.android.ui.AndroidRenderDelegate
import com.kemp.core.Entity
import com.kemp.core.ecs.components.TouchButtonsComponent
import com.kemp.core.input.touch.TouchButton

@All(TouchButtonsComponent::class)
class AndroidTouchButtonRenderingSystem : IteratingSystem(), AndroidRenderDelegate {
    private lateinit var touchButtonsMapper: ComponentMapper<TouchButtonsComponent>
    private val unprocessedTouchButtons = mutableMapOf<Entity, List<TouchButton>>()

    override fun process(entityId: Int) {
        val touchStickComponent = touchButtonsMapper.get(entityId)

        val touchSticks = touchStickComponent.touchButtons.values.filter { it.enabled }
        unprocessedTouchButtons[entityId] = touchSticks
    }

    private val touchButtonPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.FILL
        it.color = Color.WHITE
    }

    override fun draw(canvas: Canvas): Boolean {
        for (touchButtons in unprocessedTouchButtons.values) {
            for (touchButton in touchButtons) {
                if (touchButton.active) {
                    touchButtonPaint.color = Color.DKGRAY
                } else {
                    touchButtonPaint.color = Color.WHITE
                }

                canvas.drawRect(
                    touchButton.x,
                    touchButton.y,
                    touchButton.x + touchButton.width,
                    touchButton.y + touchButton.height,
                    touchButtonPaint)
            }
        }

        unprocessedTouchButtons.clear()
        return true
    }
}