package com.kemp.android.ecs.systems

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.android.ui.AndroidRenderDelegate
import com.kemp.core.Entity
import com.kemp.core.ecs.components.TouchSticksComponent
import com.kemp.core.input.touch.TouchStick

@All(TouchSticksComponent::class)
class AndroidTouchStickRenderingSystem : IteratingSystem(), AndroidRenderDelegate {
    private lateinit var touchSticksMapper: ComponentMapper<TouchSticksComponent>
    private val unprocessedTouchSticks = mutableMapOf<Entity, List<TouchStick>>()

    override fun process(entityId: Int) {
        val touchStickComponent = touchSticksMapper.get(entityId)

        val touchSticks = touchStickComponent.touchSticks.values.filter { it.enabled }
        unprocessedTouchSticks[entityId] = touchSticks
    }

    private val touchStickRangePaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.strokeWidth = 2f
        it.color = Color.WHITE
    }

    private val touchStickPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.FILL
        it.color = Color.WHITE
    }

    override fun draw(canvas: Canvas): Boolean {
        for (touchSticks in unprocessedTouchSticks.values) {
            for (touchStick in touchSticks) {
                if (touchStick.active) {
                    touchStickPaint.color = Color.DKGRAY
                } else {
                    touchStickPaint.color = Color.WHITE
                }

                canvas.drawCircle(touchStick.x, touchStick.y, touchStick.radiusPx, touchStickRangePaint)
                canvas.drawCircle(
                    touchStick.x - touchStick.relativeStickPosition.x,
                    touchStick.y - touchStick.relativeStickPosition.y,
                    touchStick.radiusPx / 2f,
                    touchStickPaint)
            }
        }

        unprocessedTouchSticks.clear()
        return true
    }
}