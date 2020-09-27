package com.kemp.core.scene

import com.kemp.core.Entity
import com.kemp.core.SceneEntity
import com.kemp.core.rendering.ImageBasedLighting

interface Scene {
    fun mainCamera(): Entity
    fun addEntity(entity: SceneEntity)
    fun addEntities(entities: List<SceneEntity>)
    fun removeEntity(entity: SceneEntity)
    fun removeEntities(entities: List<SceneEntity>)
    fun imageBasedLighting(ibl: ImageBasedLighting)
}