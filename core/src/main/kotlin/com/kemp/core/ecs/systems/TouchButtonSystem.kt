package com.kemp.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.Kemp
import com.kemp.core.ecs.components.TouchButtonsComponent

@All(TouchButtonsComponent::class)
class TouchButtonSystem : IteratingSystem() {
    private lateinit var touchButtonsMapper: ComponentMapper<TouchButtonsComponent>

    override fun process(entityId: Int) {
        val touchButtonsComponent = touchButtonsMapper.get(entityId)

        val touchButtons = touchButtonsComponent.touchButtons.values.filter { it.enabled }

        for (touchButton in touchButtons) {
            val pointerPos = Kemp.touchInput.pointer(0)
            val pointerPressed = Kemp.touchInput.pointerPressed(0)

            touchButton.touchAt(pointerPressed, pointerPos.x, pointerPos.y)
        }
    }
}