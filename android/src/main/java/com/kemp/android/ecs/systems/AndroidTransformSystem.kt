package com.kemp.android.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.annotations.One
import com.artemis.systems.IteratingSystem
import com.google.android.filament.Engine
import com.google.android.filament.TransformManager
import com.kemp.core.ecs.components.CameraNodeComponent
import com.kemp.core.ecs.components.EntityAssociationComponent
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.utils.Float3
import com.kemp.core.utils.lookTowards

@All(TransformComponent::class, EntityAssociationComponent::class)
@One(NodeComponent::class, CameraNodeComponent::class)
class AndroidTransformSystem(private val engine: Engine, private val transformManager: TransformManager) : IteratingSystem() {
    private lateinit var transformMapper: ComponentMapper<TransformComponent>
    private lateinit var nodeMapper: ComponentMapper<NodeComponent>
    private lateinit var cameraNodeMapper: ComponentMapper<CameraNodeComponent>
    private lateinit var entityAssociationMapper: ComponentMapper<EntityAssociationComponent>

    private var rotationDone = false

    override fun process(entityId: Int) {
        val transformComponent = transformMapper.get(entityId)
        val transform = transformComponent.transform

        val nodeComponent = if (nodeMapper.has(entityId)) {
            nodeMapper.get(entityId)
        } else {
            cameraNodeMapper.get(entityId)
        }

        val entityAssociationComponent = entityAssociationMapper.get(entityId)
        val filamentEntity = entityAssociationComponent.implementationEntity

        if (nodeComponent is NodeComponent) {
            // It's a Filament renderable
            filamentEntity?.let {
                val i = transformManager.getInstance(filamentEntity)
                val transformArray = transform.array
                transformManager.setTransform(i, transformArray)
            }
        } else if (nodeComponent is CameraNodeComponent) {
            // It's a Filament camera
            filamentEntity?.let {
                val position = transform.position
                val forward = transform.forward
                val up = transform.up

                val camera = engine.getCameraComponent(filamentEntity)

                // FIXME: this creates a new Float3 for every call
                val center = position + forward
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