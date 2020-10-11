package com.kemp.core.ui

import com.kemp.core.Entity
import com.kemp.core.Name
import com.kemp.core.component
import com.kemp.core.ecs.components.TouchSticksComponent
import com.kemp.core.input.touch.TouchStick
import com.kemp.core.mapper

class Ui {
    fun createTouchStick(attachTo: Entity, name: Name, x: Float, y: Float, radiusPx: Float) {
        val touchStickMapper = mapper<TouchSticksComponent>()

        val touchStickComponent =
            if (touchStickMapper.has(attachTo)) {
                touchStickMapper.get(attachTo)
            } else {
                touchStickMapper.create(attachTo)
            }

        touchStickComponent.touchSticks[name] = TouchStick(x, y, radiusPx)
    }

    fun enableTouchStick(touchStick: Name, attachedTo: Entity) {
        val touchStickComponent = attachedTo.component<TouchSticksComponent>()
        touchStickComponent.touchSticks[touchStick]?.enabled = true
    }

    fun disableTouchStick(touchStick: Name, attachedTo: Entity) {
        val touchStickComponent = attachedTo.component<TouchSticksComponent>()
        touchStickComponent.touchSticks[touchStick]?.enabled = false
    }
}