package com.kemp.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.Kemp
import com.kemp.core.ecs.components.TouchStickComponent

@All(TouchStickComponent::class)
class TouchStickSystem : IteratingSystem() {
    private lateinit var touchStickMapper: ComponentMapper<TouchStickComponent>

    override fun process(entityId: Int) {
        val touchStickComponent = touchStickMapper.get(entityId)
        if (!touchStickComponent.enabled) return

        val touchStick = touchStickComponent.touchStick
        val pointerPos = Kemp.touchInput.pointer(0)
        val pointerPressed = Kemp.touchInput.pointerPressed(0)

        touchStick.touchAt(pointerPressed, pointerPos.x, pointerPos.y)
    }
}