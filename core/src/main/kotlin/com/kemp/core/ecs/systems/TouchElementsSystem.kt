package com.kemp.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.Kemp
import com.kemp.core.ecs.components.TouchElementsComponent

@All(TouchElementsComponent::class)
class TouchElementsSystem : IteratingSystem() {
    private lateinit var touchElementsMapper: ComponentMapper<TouchElementsComponent>

    override fun process(entityId: Int) {
        val touchElementsComponent = touchElementsMapper.get(entityId)
        val touchElements = touchElementsComponent.touchElements.values.filter { it.enabled }

        for (touchElement in touchElements) {
            val pointerPos = Kemp.touchInput.pointer(0)
            val pointerPressed = Kemp.touchInput.pointerPressed(0)

            touchElement.touchAt(pointerPressed, pointerPos.x, pointerPos.y)
        }
    }
}