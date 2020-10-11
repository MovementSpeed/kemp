package com.kemp.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.Kemp
import com.kemp.core.ecs.components.TouchSticksComponent

@All(TouchSticksComponent::class)
class TouchStickSystem : IteratingSystem() {
    private lateinit var touchSticksMapper: ComponentMapper<TouchSticksComponent>

    override fun process(entityId: Int) {
        val touchStickComponent = touchSticksMapper.get(entityId)

        val touchSticks = touchStickComponent.touchSticks.values.filter { it.enabled }

        for (touchStick in touchSticks) {
            val pointerPos = Kemp.touchInput.pointer(0)
            val pointerPressed = Kemp.touchInput.pointerPressed(0)

            touchStick.touchAt(pointerPressed, pointerPos.x, pointerPos.y)
        }
    }
}