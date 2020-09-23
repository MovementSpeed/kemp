package com.kemp.core.playground

import com.artemis.WorldConfigurationBuilder
import com.kemp.core.Kemp
import com.kemp.core.app.Game
import com.kemp.core.ecs.components.CameraNodeComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.scene.Scene
import com.kemp.core.utils.Float3
import kotlinx.coroutines.launch

class PlaygroundGame : Game {
    override fun worldConfig(worldConfigurationBuilder: WorldConfigurationBuilder) {
        worldConfigurationBuilder.with(RotateObjectsSystem())
    }

    override fun ready(scene: Scene) {
        val camera = scene.mainCamera()

        val transformMapper = Kemp.world.getMapper(TransformComponent::class.java)
        val cameraTransform = transformMapper.get(camera)
        cameraTransform.transform.position(Float3(0f, 3f, -20f))

        Kemp.coroutineScope.launch {
            val model = Kemp.assets.loadModel("models", "model.glb")
            model?.let { scene.addEntities(model.entities()) }
        }
    }

    override fun update(delta: Float) {
    }
}