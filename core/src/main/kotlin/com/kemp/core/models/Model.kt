package com.kemp.core.models

import com.kemp.core.Entity
import com.kemp.core.utils.Box

interface Model {
    fun root(): Entity
    fun lights(): List<Entity>
    fun cameras(): List<Entity>
    fun entity(name: String): Entity
    fun entities(): List<Entity>
    fun entities(name: String): List<Entity>
    fun entitiesStartingWith(name: String): List<Entity>
    fun materials(): List<MaterialInstance>
    fun boundingBox(): Box
    fun nameOf(entity: Entity): String
    fun animator(): ModelAnimator
}