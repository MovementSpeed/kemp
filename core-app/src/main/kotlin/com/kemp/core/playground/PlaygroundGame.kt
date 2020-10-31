package com.kemp.core.playground

import com.artemis.WorldConfigurationBuilder
import com.kemp.core.*
import com.kemp.core.app.Game
import com.kemp.core.config.rendering.AntiAliasing
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.rendering.ui.DefaultTouchStickRenderer
import com.kemp.core.scene.Scene
import com.kemp.core.utils.Float3
import kotlinx.coroutines.launch

class PlaygroundGame : Game {
    override fun worldConfig(worldConfigurationBuilder: WorldConfigurationBuilder) {
        worldConfigurationBuilder.with(PlayerControllerSystem())
    }

    override fun ready(scene: Scene) {
        val graphics = Kemp.graphicsConfig
        graphics.antiAliasing = AntiAliasing.FXAA

        val bloom = graphics.bloomOptions
        bloom.enabled = true
        bloom.strength = 0.1f

        val ao = graphics.ambientOcclusionOptions
        ao.enabled = true
        ao.resolution = 0.75f
        ao.intensity = 3f
        ao.power = 3f
        ao.radius = 0.75f

        Kemp.graphicsConfig.configChanged()

        val camera = scene.mainCamera()
        val cameraTransform = camera.component<TransformComponent>()

        cameraTransform.transform
            .position(Float3(0f, 5f, -13f))
            .rotate(Float3(20f, 0f, 0f))

        Kemp.coroutineScope.launch {
            val assets = Kemp.assets

            val plane = assets.loadModel("models", "basic_plane.glb")
            plane?.apply {
                scene.addEntities(entities())

                val entity = entity()
                val entityTransform = entity.component<TransformComponent>()
                entityTransform.transform
                    .position(Position(0f))
                    .scale(Scale(2f))
            }

            val bob = assets.loadModel("models", "bob.glb")
            bob?.apply {
                scene.addEntities(entities())

                val entity = entity()

                val entityTransform = entity.component<TransformComponent>()
                entityTransform.transform
                    .position(Position(0f, 0f, 0f))
                    .rotate(Rotation(0f, 90f, 0f))

                val playerMapper = mapper<PlayerComponent>()
                playerMapper.create(entity)

                val screenWidth = graphics.width
                val radius = 150f

                Kemp.ui.createTouchStick(
                    entity,
                    "rotation",
                    screenWidth / 2f,
                    radius + 64f,
                    radius, DefaultTouchStickRenderer())
            }

            val ibl = assets.loadIndirectLight("lighting", "environment_ibl.ktx")
            scene.imageBasedLighting(ibl)

            val env = assets.loadSkybox("lighting", "environment_skybox.ktx")
            scene.environment(env)
        }
    }

    override fun update(delta: Float) {
    }
}