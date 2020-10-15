package com.kemp.android.models

import com.google.android.filament.Engine
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.FilamentAsset
import com.kemp.core.Entity
import com.kemp.core.SceneEntity
import com.kemp.core.models.MaterialInstance
import com.kemp.core.models.Model
import com.kemp.core.models.ModelAnimator
import com.kemp.core.utils.Box

class AndroidModel(private val assetLoader: AssetLoader, private val filamentAsset: FilamentAsset) : Model {
    var entity: Entity = -1

    override fun entity(): Entity {
        return entity
    }

    override fun root(): SceneEntity {
        return filamentAsset.root
    }

    override fun lights(): List<SceneEntity> {
        return filamentAsset.lightEntities.toList()
    }

    override fun cameras(): List<SceneEntity> {
        return filamentAsset.cameraEntities.toList()
    }

    override fun entity(name: String): SceneEntity {
        return filamentAsset.getFirstEntityByName(name)
    }

    override fun entities(): List<SceneEntity> {
        return filamentAsset.entities.toList()
    }

    override fun entities(name: String): List<SceneEntity> {
        return filamentAsset.getEntitiesByName(name).toList()
    }

    override fun entitiesStartingWith(name: String): List<SceneEntity> {
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

    override fun nameOf(entity: SceneEntity): String {
        return filamentAsset.getName(entity)
    }

    override fun animator(): ModelAnimator {
        return AndroidModelAnimator(filamentAsset.animator)
    }

    override fun dispose() {
        assetLoader.destroyAsset(filamentAsset)
    }
}