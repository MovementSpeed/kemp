package com.kemp.android.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.One
import com.artemis.systems.IteratingSystem
import com.google.android.filament.Engine
import com.google.android.filament.TransformManager
import com.kemp.core.ecs.components.CameraComponent
import com.kemp.core.ecs.components.EntityAssociationComponent
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.utils.Pool

@All(TransformComponent::class, EntityAssociationComponent::class)
@One(NodeComponent::class, CameraComponent::class)
class AndroidTransformSystem(private val engine: Engine, private val transformManager: TransformManager) :
    IteratingSystem() {
    private lateinit var transformMapper: ComponentMapper<TransformComponent>
    private lateinit var nodeMapper: ComponentMapper<NodeComponent>
    private lateinit var cameraMapper: ComponentMapper<CameraComponent>
    private lateinit var entityAssociationMapper: ComponentMapper<EntityAssociationComponent>
    private var float3 = Pool.useFloat3()

    override fun process(entityId: Int) {
        val transformComponent = transformMapper.get(entityId)
        val transform = transformComponent.transform
        transform.update()

        val component =
            if (nodeMapper.has(entityId)) {
                nodeMapper.get(entityId)
            } else {
                cameraMapper.get(entityId)
            }

        val entityAssociationComponent = entityAssociationMapper.get(entityId)
        val filamentEntity = entityAssociationComponent.implementationEntity

        if (component is NodeComponent) {
            // It's a Filament renderable
            filamentEntity?.let {
                val i = transformManager.getInstance(filamentEntity)
                val transformArray = transform.array
                transformManager.setTransform(i, transformArray)
            }
        } else if (component is CameraComponent) {
            // It's a Filament camera
            filamentEntity?.let {
                val position = transform.position
                val forward = transform.forward
                val up = transform.up

                val camera = engine.getCameraComponent(filamentEntity)

                float3.x = position.x + forward.x
                float3.y = position.y + forward.y
                float3.z = position.z + forward.z

                val center = float3
                camera?.lookAt(
                    position.x.toDouble(),
                    position.y.toDouble(),
                    position.z.toDouble(),

                    center.x.toDouble(),
                    center.y.toDouble(),
                    center.z.toDouble(),

                    up.x.toDouble(),
                    up.y.toDouble(),
                    up.z.toDouble()
                )
            }
        }
    }
}