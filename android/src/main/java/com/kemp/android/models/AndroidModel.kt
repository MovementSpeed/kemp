package com.kemp.android.models

import com.google.android.filament.gltfio.FilamentAsset
import com.kemp.core.Entity
import com.kemp.core.models.MaterialInstance
import com.kemp.core.models.Model
import com.kemp.core.models.ModelAnimator
import com.kemp.core.utils.Box

class AndroidModel(private val filamentAsset: FilamentAsset) : Model {
    override fun root(): Entity {
        return filamentAsset.root
    }

    override fun lights(): List<Entity> {
        return filamentAsset.lightEntities.toList()
    }

    override fun cameras(): List<Entity> {
        return filamentAsset.cameraEntities.toList()
    }

    override fun entity(name: String): Entity {
        return filamentAsset.getFirstEntityByName(name)
    }

    override fun entities(): List<Entity> {
        return filamentAsset.entities.toList()
    }

    override fun entities(name: String): List<Entity> {
        return filamentAsset.getEntitiesByName(name).toList()
    }

    override fun entitiesStartingWith(name: String): List<Entity> {
        return filamentAsset.getEntitiesByPrefix(name).toList()
    }

    override fun materials(): List<MaterialInstance> {
        val materialsWrap = mutableListOf<MaterialInstance>()
        filamentAsset.materialInstances.forEach { materialsWrap.add(AndroidMaterialInstance(it)) }
        return materialsWrap
    }

    override fun boundingBox(): Box {
        return Box(filamentAsset.boundingBox.center, filamentAsset.boundingBox.halfExtent)
    }

    override fun nameOf(entity: Entity): String {
        return filamentAsset.getName(entity)
    }

    override fun animator(): ModelAnimator {
        return AndroidModelAnimator(filamentAsset.animator)
    }
}