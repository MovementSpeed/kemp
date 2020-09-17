package com.kemp.android.scene

import com.kemp.android.FilamentScene
import com.kemp.core.Entity
import com.kemp.core.scene.Scene

class AndroidScene(private val scene: FilamentScene): Scene {
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
}