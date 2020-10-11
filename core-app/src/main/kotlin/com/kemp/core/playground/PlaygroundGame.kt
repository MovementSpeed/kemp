package com.kemp.core.playground

import com.artemis.WorldConfigurationBuilder
import com.kemp.core.Kemp
import com.kemp.core.app.Game
import com.kemp.core.component
import com.kemp.core.config.AntiAliasing
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.input.Keys
import com.kemp.core.input.touch.TouchStick
import com.kemp.core.models.Model
import com.kemp.core.scene.Scene
import com.kemp.core.utils.Float3
import com.kemp.core.utils.length
import kotlinx.coroutines.launch

class PlaygroundGame : Game {
    private var model: Model? = null

    override fun worldConfig(worldConfigurationBuilder: WorldConfigurationBuilder) {
        worldConfigurationBuilder.with(RotateObjectsSystem())
    }

    override fun ready(scene: Scene) {
        Kemp.graphicsConfig.antiAliasing = AntiAliasing.FXAA
        Kemp.graphicsConfig.bloomOptions.enabled = true
        Kemp.graphicsConfig.ambientOcclusionOptions.enabled = true
        Kemp.graphicsConfig.configChanged()

        val camera = scene.mainCamera()
        val cameraTransform = camera.component<TransformComponent>()
        cameraTransform.transform.position(Float3(-200f, 90f, -200f))
            .rotate(Float3(0f, 45f, 0f))

        Kemp.coroutineScope.launch {
            model = Kemp.assets.loadModel("models", "test_2.glb")
            model?.apply {
                scene.addEntities(entities())

                val entity = entity()
                val entityTransform = entity.component<TransformComponent>()
                entityTransform.transform.scale(Float3(4f, 4f, 4f))

                val screenWidth = Kemp.graphicsConfig.width
                val screenHeight = Kemp.graphicsConfig.height
                val radius = 200f

                Kemp.ui.createTouchStick(
                    entity,
                    "rotation",
                    screenWidth / 2f,
                    screenHeight.toFloat() - radius - 64f,
                    radius)
            }

            val ibl = Kemp.assets.loadIndirectLight("lighting", "environment_ibl.ktx")
            scene.imageBasedLighting(ibl)

            val env = Kemp.assets.loadSkybox("lighting", "environment_skybox.ktx")
            scene.environment(env)
        }
    }

    override fun update(delta: Float) {
    }
}