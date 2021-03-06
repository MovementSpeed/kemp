package com.kemp.core.playground

import com.artemis.WorldConfigurationBuilder
import com.kemp.core.*
import com.kemp.core.app.Game
import com.kemp.core.config.rendering.AntiAliasing
import com.kemp.core.config.rendering.GraphicsConfig
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.io.Assets
import com.kemp.core.rendering.models.Model
import com.kemp.core.rendering.ui.DefaultTouchStickRenderer
import com.kemp.core.scene.Scene
import com.kemp.core.utils.Float3
import kotlinx.coroutines.launch

class PlaygroundGame : Game {
    private var bobModel: Model? = null
    private val cameraControllerSystem = CameraControllerSystem()

    override fun worldConfig(worldConfigurationBuilder: WorldConfigurationBuilder) {
        worldConfigurationBuilder
            .with(PlayerControllerSystem())
            .with(cameraControllerSystem)
    }

    override fun ready(scene: Scene) {
        val graphics = Kemp.graphicsConfig
        graphics.antiAliasing = AntiAliasing.FXAA

        val bloom = graphics.bloomOptions
        bloom.enabled = true
        bloom.strength = 0.1f

        val ao = graphics.ambientOcclusionOptions
        ao.enabled = true
        ao.power = 1.5f

        Kemp.graphicsConfig.configChanged()

        val camera = Kemp.world.create()
        scene.createCamera(camera)

        val cameraTransform = camera.component<TransformComponent>()

        cameraTransform.transform
            .position(Float3(0f, 5f, -13f))
            .rotate(Float3(20f, 0f, 0f))

        Kemp.coroutineScope.launch {
            val assets = Kemp.assets
            createPlane(assets, scene)

            val bob = createBob(assets, scene, graphics)
            createWeapon(attachTo = bob, assets, scene)

            val ibl = assets.loadIndirectLight("lighting", "_ibl.ktx")
            ibl.intensity(70_000f)
            ibl.rotate(Float3(0f, 0f, 0f))

            scene.imageBasedLighting(ibl)

            val env = assets.loadSkybox("lighting", "_skybox.ktx")
            scene.environment(env)
        }
    }

    private var anim = 0f

    override fun update(delta: Float) {
        /*val bobAnimator = bobModel?.animator()
        val durationSeconds = bobAnimator?.animationDuration(0)

        durationSeconds?.let {
            anim += 0.01f
            if (anim > durationSeconds) {
                anim = 0f
            }

            bobAnimator.animate(0, anim)
        }*/
    }

    private suspend fun createPlane(assets: Assets, scene: Scene) {
        val plane = assets.loadModel("models", "basic_plane.glb")
        scene.addEntities(plane.entities())
        val entity = plane.entity()

        val entityTransform = entity.component<TransformComponent>()
        entityTransform.transform
            .position(Position(0f))
            .scale(Scale(2f))
    }

    private suspend fun createBob(assets: Assets, scene: Scene, graphics: GraphicsConfig): SceneEntity {
        val bob = assets.loadModel("models", "bob.glb")

        scene.addEntities(bob.entities())
        val bobEntity = bob.entity()

        val playerMapper = mapper<PlayerComponent>()
        playerMapper.create(bobEntity)

        val screenWidth = graphics.width
        val screenHeight = graphics.height
        val radius = 150f

        Kemp.ui.createTouchStick(
            bobEntity,
            "rotation",
            screenWidth / 2f,
            screenHeight - radius - 64f,
            radius, DefaultTouchStickRenderer())

        cameraControllerSystem.target = bobEntity
        bobModel = bob
        return bob.root()
    }

    private suspend fun createWeapon(attachTo: SceneEntity, assets: Assets, scene: Scene) {
        val weapon = assets.loadModel("models", "mac11.glb")
        scene.addEntities(weapon.entities())

        val weaponEntity = weapon.entity()
        val weaponSceneEntity = weapon.root()

        scene.reparent(weaponSceneEntity, attachTo)

        val entityTransform = weaponEntity.component<TransformComponent>()
        entityTransform.transform
            .translate(Position(0.7f, 1f, -1f))
    }
}