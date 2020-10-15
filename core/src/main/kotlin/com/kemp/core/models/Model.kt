package com.kemp.core.models

import com.kemp.core.Entity
import com.kemp.core.SceneEntity
import com.kemp.core.interfaces.Disposable
import com.kemp.core.utils.Box

interface Model : Disposable {
    fun entity(): Entity
    fun root(): SceneEntity
    fun lights(): List<SceneEntity>
    fun cameras(): List<SceneEntity>
    fun entity(name: String): SceneEntity
    fun entities(): List<SceneEntity>
    fun entities(name: String): List<SceneEntity>
    fun entitiesStartingWith(name: String): List<SceneEntity>
    fun materials(): List<MaterialInstance>
    fun boundingBox(): Box
    fun nameOf(entity: SceneEntity): String
    fun animator(): ModelAnimator
}