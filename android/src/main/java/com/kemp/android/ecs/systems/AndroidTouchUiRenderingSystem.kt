package com.kemp.android.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.Kemp
import com.kemp.core.ecs.components.TouchElementsComponent

@All(TouchElementsComponent::class)
class AndroidTouchUiRenderingSystem : IteratingSystem() {
    private lateinit var touchElementsMapper: ComponentMapper<TouchElementsComponent>

    override fun process(entityId: Int) {
        val touchElementsComponent = touchElementsMapper.get(entityId) ?: return

        val touchElements = touchElementsComponent.touchElements.values.filter { it.enabled }
        for (touchElement in touchElements) {
            Kemp.ui.renderer2D?.let {
                touchElement.touchElementRenderer?.render(it, touchElement)
            }
        }
    }
}