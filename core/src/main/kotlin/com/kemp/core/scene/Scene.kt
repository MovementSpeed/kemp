package com.kemp.core.scene

import com.kemp.core.Entity

interface Scene {
    fun addEntity(entity: Entity)
    fun addEntities(entities: List<Entity>)
    fun removeEntity(entity: Entity)
    fun removeEntities(entities: List<Entity>)
}