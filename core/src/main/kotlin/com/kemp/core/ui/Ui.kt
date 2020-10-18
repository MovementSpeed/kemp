package com.kemp.core.ui

import com.kemp.core.Entity
import com.kemp.core.Name
import com.kemp.core.component
import com.kemp.core.ecs.components.TouchButtonsComponent
import com.kemp.core.ecs.components.TouchSticksComponent
import com.kemp.core.input.touch.TouchButton
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

    fun createTouchButton(attachTo: Entity, name: Name, x: Float, y: Float, width: Float, height: Float) {
        val touchButtonMapper = mapper<TouchButtonsComponent>()

        val touchButtonComponent =
            if (touchButtonMapper.has(attachTo)) {
                touchButtonMapper.get(attachTo)
            } else {
                touchButtonMapper.create(attachTo)
            }

        touchButtonComponent.touchButtons[name] = TouchButton(x, y, width, height)
    }

    fun enableTouchButton(touchButton: Name, attachedTo: Entity) {
        val touchButtonComponent = attachedTo.component<TouchButtonsComponent>()
        touchButtonComponent.touchButtons[touchButton]?.enabled = true
    }

    fun disableTouchButton(touchButton: Name, attachedTo: Entity) {
        val touchButtonComponent = attachedTo.component<TouchButtonsComponent>()
        touchButtonComponent.touchButtons[touchButton]?.enabled = false
    }
}