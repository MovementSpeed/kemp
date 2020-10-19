package com.kemp.core.playground

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.components.TouchElementsComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.input.touch.TouchStick
import com.kemp.core.utils.Pool

@All(TransformComponent::class, NodeComponent::class, TouchElementsComponent::class, PlayerComponent::class)
class RotatePlayerSystem : IteratingSystem() {
    private lateinit var transformMapper: ComponentMapper<TransformComponent>
    private lateinit var nodeMapper: ComponentMapper<NodeComponent>
    private lateinit var touchElementsMapper: ComponentMapper<TouchElementsComponent>

    private var float3 = Pool.useFloat3()

    override fun process(entityId: Int) {
        val transformComponent = transformMapper.get(entityId)
        val transform = transformComponent.transform

        val touchSticksComponent = touchElementsMapper.get(entityId)
        val touchStick = touchSticksComponent.touchElements["rotation"] as? TouchStick?

        if (touchStick != null) {
            float3.x = 0f
            float3.y = touchStick.stickVector.x
            float3.z = 0f

            transform.rotate(float3)
        }
    }
}