package com.kemp.core.playground

import com.artemis.WorldConfigurationBuilder
import com.kemp.core.Kemp
import com.kemp.core.app.Game
import com.kemp.core.component
import com.kemp.core.config.AntiAliasing
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
        Kemp.graphicsConfig.antiAliasing = AntiAliasing.FXAA
        val camera = scene.mainCamera()

        val cameraTransform = camera.component<TransformComponent>()
        cameraTransform.transform.position(Float3(0f, 3f, -40f))

        Kemp.coroutineScope.launch {
            val model = Kemp.assets.loadModel("models", "model.glb")
            model?.let { scene.addEntities(model.entities()) }

            val ibl = Kemp.assets.loadIndirectLight("lighting", "environment_ibl.ktx")
            scene.imageBasedLighting(ibl)
        }
    }

    override fun update(delta: Float) {
    }
}