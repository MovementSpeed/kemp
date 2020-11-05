package com.kemp.android.scene

import com.google.android.filament.Camera
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.kemp.android.FilamentScene
import com.kemp.android.FilamentView
import com.kemp.android.rendering.effects.AndroidEnvironment
import com.kemp.android.rendering.effects.AndroidImageBasedLighting
import com.kemp.core.Entity
import com.kemp.core.Kemp
import com.kemp.core.SceneEntity
import com.kemp.core.ecs.components.CameraComponent
import com.kemp.core.ecs.components.EntityAssociationComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.interfaces.Disposable
import com.kemp.core.mapper
import com.kemp.core.rendering.effects.Environment
import com.kemp.core.rendering.effects.ImageBasedLighting
import com.kemp.core.scene.Scene

class AndroidScene(private val engine: Engine, private val scene: FilamentScene, private val view: FilamentView): Scene {
    private val disposables = mutableListOf<Disposable>()

    override fun createCamera(attachTo: Entity, fovDegrees: Double, near: Double, far: Double) {
        val em = EntityManager.get()

        val filamentCameraEntity = em.create()
        val filamentCamera = engine.createCamera(filamentCameraEntity)

        view.camera = filamentCamera

        val entityAssociation = mapper<EntityAssociationComponent>().create(attachTo)
        entityAssociation.implementationEntity = filamentCameraEntity

        mapper<CameraComponent>().create(attachTo)
        mapper<TransformComponent>().create(attachTo)

        val aspectRatio = Kemp.graphicsConfig.width.toDouble() / Kemp.graphicsConfig.height.toDouble()

        filamentCamera.setExposure(16f, 1f / 125f, 100f)
        filamentCamera.setProjection(fovDegrees, aspectRatio, near, far, Camera.Fov.VERTICAL)

        disposables.add(object : Disposable {
            override fun dispose() {
                engine.destroyCamera(filamentCamera)
            }
        })
    }

    override fun addEntity(entity: SceneEntity) {
        scene.addEntity(entity)
    }

    override fun addEntities(entities: List<SceneEntity>) {
        scene.addEntities(entities.toIntArray())
    }

    override fun removeEntity(entity: SceneEntity) {
        scene.removeEntity(entity)
    }

    override fun removeEntities(entities: List<SceneEntity>) {
        scene.removeEntities(entities.toIntArray())
    }

    override fun reparent(entity: SceneEntity, to: SceneEntity) {
        val tm = engine.transformManager

        val e1 = tm.getInstance(entity)
        val e2 = tm.getInstance(to)

        tm.setParent(e1, e2)
    }

    override fun imageBasedLighting(ibl: ImageBasedLighting) {
        val androidIbl = ibl as AndroidImageBasedLighting
        scene.indirectLight = androidIbl.indirectLight
    }

    override fun environment(environment: Environment) {
        val androidEnvironment = environment as AndroidEnvironment
        scene.skybox = androidEnvironment.skybox
    }

    override fun dispose() {
        disposables.forEach { it.dispose() }
    }
}