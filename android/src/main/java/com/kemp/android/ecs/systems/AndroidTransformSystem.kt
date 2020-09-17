package com.kemp.android.ecs.systems

import com.google.android.filament.Engine
import com.google.android.filament.TransformManager
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.systems.TransformSystem

class AndroidTransformSystem(private val engine: Engine, private val transformManager: TransformManager) : TransformSystem() {
    override fun process(entityId: Int) {
        val transformComponent = transformMapper.get(entityId)
        val transform = transformComponent.transform

        val nodeComponent = if (nodeMapper.has(entityId)) {
            nodeMapper.get(entityId)
        } else {
            cameraNodeMapper.get(entityId)
        }

        if (nodeComponent is NodeComponent) {
            // It's a Filament renderable
            val i = transformManager.getInstance(entityId)
            val transformArray = transform.array
            transformManager.setTransform(i, transformArray)
        } else {
            // It's a Filament camera
            val position = transform.position
            val forward = transform.forward
            val up = transform.up

            val camera = engine.getCameraComponent(entityId)
            camera?.lookAt(
                position.x.toDouble(),
                position.y.toDouble(),
                position.z.toDouble(),

                forward.x.toDouble(),
                forward.y.toDouble(),
                forward.z.toDouble(),

                up.x.toDouble(),
                up.y.toDouble(),
                up.z.toDouble()
            )
        }
    }
}