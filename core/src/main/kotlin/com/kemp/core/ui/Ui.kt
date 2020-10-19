package com.kemp.core.ui

import com.kemp.core.Entity
import com.kemp.core.Name
import com.kemp.core.component
import com.kemp.core.ecs.components.TouchElementsComponent
import com.kemp.core.input.touch.TouchButton
import com.kemp.core.input.touch.TouchStick
import com.kemp.core.mapper
import com.kemp.core.rendering.ui.Renderer2D
import com.kemp.core.rendering.ui.TouchElementRenderer

class Ui {
    var renderer2D: Renderer2D? = null

    fun createTouchStick(attachTo: Entity, name: Name, x: Float, y: Float, radiusPx: Float, touchElementRenderer: TouchElementRenderer) {
        val touchStickMapper = mapper<TouchElementsComponent>()

        val touchStickComponent =
            if (touchStickMapper.has(attachTo)) {
                touchStickMapper.get(attachTo)
            } else {
                touchStickMapper.create(attachTo)
            }

        val touchStick = TouchStick(x, y, radiusPx)
        touchStick.touchElementRenderer = touchElementRenderer
        touchStickComponent.touchElements[name] = touchStick
    }

    fun enableTouchStick(touchStick: Name, attachedTo: Entity) {
        val touchStickComponent = attachedTo.component<TouchElementsComponent>()
        touchStickComponent.touchElements[touchStick]?.enabled = true
    }

    fun disableTouchStick(touchStick: Name, attachedTo: Entity) {
        val touchStickComponent = attachedTo.component<TouchElementsComponent>()
        touchStickComponent.touchElements[touchStick]?.enabled = false
    }

    fun createTouchButton(attachTo: Entity, name: Name, x: Float, y: Float, width: Float, height: Float, touchElementRenderer: TouchElementRenderer) {
        val touchButtonMapper = mapper<TouchElementsComponent>()

        val touchButtonComponent =
            if (touchButtonMapper.has(attachTo)) {
                touchButtonMapper.get(attachTo)
            } else {
                touchButtonMapper.create(attachTo)
            }

        val touchButton = TouchButton(x, y, width, height)
        touchButton.touchElementRenderer = touchElementRenderer
        touchButtonComponent.touchElements[name] = touchButton
    }

    fun enableTouchButton(touchButton: Name, attachedTo: Entity) {
        val touchButtonComponent = attachedTo.component<TouchElementsComponent>()
        touchButtonComponent.touchElements[touchButton]?.enabled = true
    }

    fun disableTouchButton(touchButton: Name, attachedTo: Entity) {
        val touchButtonComponent = attachedTo.component<TouchElementsComponent>()
        touchButtonComponent.touchElements[touchButton]?.enabled = false
    }
}