package com.kemp.core.playground

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.utils.Float3
import com.kemp.core.utils.Pool

@All(TransformComponent::class, NodeComponent::class)
class RotateObjectsSystem : IteratingSystem() {
    private lateinit var transformMapper: ComponentMapper<TransformComponent>
    private lateinit var nodeMapper: ComponentMapper<NodeComponent>

    private var float3 = Pool.useFloat3()

    override fun process(entityId: Int) {
        val transformComponent = transformMapper.get(entityId)
        val transform = transformComponent.transform

        val nodeComponent = nodeMapper.get(entityId)
        val name = nodeComponent.name

        if (name == "test.glb_root") {
            float3.x = 0f
            float3.y = 0.4f
            float3.z = 0f

            transform.rotate(float3)
        }
    }
}