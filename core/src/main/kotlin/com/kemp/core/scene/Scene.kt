package com.kemp.core.scene

import com.kemp.core.Entity
import com.kemp.core.SceneEntity
import com.kemp.core.interfaces.Disposable
import com.kemp.core.rendering.effects.Environment
import com.kemp.core.rendering.effects.ImageBasedLighting

interface Scene : Disposable {
    fun createCamera(attachTo: Entity, fovDegrees: Double = 45.0, near: Double = 0.01, far: Double = 1000.0)
    fun addEntity(entity: SceneEntity)
    fun addEntities(entities: List<SceneEntity>)
    fun removeEntity(entity: SceneEntity)
    fun removeEntities(entities: List<SceneEntity>)
    fun imageBasedLighting(ibl: ImageBasedLighting)
    fun environment(environment: Environment)
}