package com.kemp.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.One
import com.artemis.systems.IteratingSystem
import com.kemp.core.ecs.components.CameraNodeComponent
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.components.TransformComponent

@All(TransformComponent::class)
@One(NodeComponent::class, CameraNodeComponent::class)
abstract class TransformSystem : IteratingSystem() {
    protected lateinit var transformMapper: ComponentMapper<TransformComponent>
    protected lateinit var nodeMapper: ComponentMapper<NodeComponent>
    protected lateinit var cameraNodeMapper: ComponentMapper<CameraNodeComponent>
}