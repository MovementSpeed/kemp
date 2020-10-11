package com.kemp.core.ui

import com.kemp.core.Entity
import com.kemp.core.Kemp
import com.kemp.core.component
import com.kemp.core.ecs.components.TouchStickComponent
import com.kemp.core.input.touch.TouchStick
import com.kemp.core.mapper

class Ui {
    fun createTouchStick(x: Float, y: Float, radiusPx: Float): Entity {
        val entity = Kemp.world.create()
        val touchStickMapper = mapper<TouchStickComponent>()
        val touchStickComponent = touchStickMapper.create(entity)
        touchStickComponent.touchStick = TouchStick(x, y, radiusPx)

        return entity
    }

    fun enableTouchStick(entity: Entity) {
        val touchStickComponent = entity.component<TouchStickComponent>()
        touchStickComponent.enabled = true
    }

    fun disableTouchStick(entity: Entity) {
        val touchStickComponent = entity.component<TouchStickComponent>()
        touchStickComponent.enabled = false
    }
}