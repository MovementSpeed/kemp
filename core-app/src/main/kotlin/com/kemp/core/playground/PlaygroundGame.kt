package com.kemp.core.playground

import com.artemis.WorldConfigurationBuilder
import com.kemp.core.Kemp
import com.kemp.core.app.Game
import com.kemp.core.component
import com.kemp.core.config.rendering.AntiAliasing
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.mapper
import com.kemp.core.rendering.ui.DefaultTouchButtonRenderer
import com.kemp.core.rendering.ui.DefaultTouchStickRenderer
import com.kemp.core.scene.Scene
import com.kemp.core.utils.Float3
import kotlinx.coroutines.launch

class PlaygroundGame : Game {
    override fun worldConfig(worldConfigurationBuilder: WorldConfigurationBuilder) {
        worldConfigurationBuilder.with(RotatePlayerSystem())
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
            val model1 = Kemp.assets.loadModel("models", "test.glb")
            model1?.apply {
                scene.addEntities(entities())

                val entity = entity()
                val entityTransform = entity.component<TransformComponent>()
                entityTransform.transform.position(Float3(100f, 0f, 0f))

                val playerMapper = mapper<PlayerComponent>()
                playerMapper.create(entity)

                val screenWidth = Kemp.graphicsConfig.width
                val screenHeight = Kemp.graphicsConfig.height
                val radius = 200f

                Kemp.ui.createTouchStick(
                    entity,
                    "rotation",
                    screenWidth / 2f,
                    screenHeight.toFloat() - radius - 64f,
                    radius, DefaultTouchStickRenderer()
                )

                Kemp.ui.createTouchButton(entity, "action", 16f, 16f, 100f, 75f, DefaultTouchButtonRenderer())
            }

            val model2 = Kemp.assets.loadModel("models", "test_2.glb")
            model2?.apply {
                scene.addEntities(entities())

                val entity = entity()
                val entityTransform = entity.component<TransformComponent>()
                entityTransform.transform.scale(Float3(4f, 4f, 4f))

                val playerMapper = mapper<PlayerComponent>()
                playerMapper.create(entity)

                val screenWidth = Kemp.graphicsConfig.width
                val radius = 150f

                Kemp.ui.createTouchStick(
                    entity,
                    "rotation",
                    screenWidth / 2f,
                    radius + 64f,
                    radius, DefaultTouchStickRenderer()
                )
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