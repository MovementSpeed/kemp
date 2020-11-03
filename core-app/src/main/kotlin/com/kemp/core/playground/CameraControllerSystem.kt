package com.kemp.core.playground

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.Entity
import com.kemp.core.ecs.components.CameraComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.utils.Float3

@All(TransformComponent::class, CameraComponent::class)
class CameraControllerSystem : IteratingSystem() {
    var target: Entity? = null
    private lateinit var transformMapper: ComponentMapper<TransformComponent>

    private val cameraPosition = Float3()
    private val cameraRotation = Float3(20f, 0f, 0f)

    override fun process(entityId: Int) {
        val t = target ?: return

        val targetTransformComponent = transformMapper.get(t)
        val targetTransform = targetTransformComponent.transform

        val transformComponent = transformMapper.get(entityId)
        val transform = transformComponent.transform

        cameraPosition.x = targetTransform.position.x
        cameraPosition.y = targetTransform.position.y + 5f
        cameraPosition.z = targetTransform.position.z - 13f

        transform
            .position(cameraPosition)
            .rotate(cameraRotation)
    }
}