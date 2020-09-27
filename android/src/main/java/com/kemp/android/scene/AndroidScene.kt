package com.kemp.android.scene

import com.kemp.android.FilamentScene
import com.kemp.android.rendering.AndroidImageBasedLighting
import com.kemp.core.Entity
import com.kemp.core.rendering.ImageBasedLighting
import com.kemp.core.scene.Scene

class AndroidScene(private val scene: FilamentScene, private val mainCamera: Entity): Scene {
    override fun mainCamera(): Entity {
        return mainCamera
    }

    override fun addEntity(entity: Entity) {
        scene.addEntity(entity)
    }

    override fun addEntities(entities: List<Entity>) {
        scene.addEntities(entities.toIntArray())
    }

    override fun removeEntity(entity: Entity) {
        scene.removeEntity(entity)
    }

    override fun removeEntities(entities: List<Entity>) {
        scene.removeEntities(entities.toIntArray())
    }

    override fun imageBasedLighting(ibl: ImageBasedLighting) {
        val androidIbl = ibl as AndroidImageBasedLighting
        scene.indirectLight = androidIbl.indirectLight
    }
}