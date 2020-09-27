package com.kemp.android.io

import android.content.Context
import com.artemis.ComponentMapper
import com.google.android.filament.Engine
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.FilamentAsset
import com.google.android.filament.gltfio.ResourceLoader
import com.google.android.filament.utils.*
import com.kemp.android.fileSeparator
import com.kemp.android.models.AndroidModel
import com.kemp.android.rendering.AndroidImageBasedLighting
import com.kemp.core.Kemp
import com.kemp.core.ecs.components.CameraNodeComponent
import com.kemp.core.ecs.components.EntityAssociationComponent
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.io.Assets
import com.kemp.core.models.Model
import com.kemp.core.rendering.ImageBasedLighting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.ByteBuffer

class AndroidAssets(private val context: Context,
                    private val engine: Engine,
                    private val assetLoader: AssetLoader,
                    private val resourceLoader: ResourceLoader) : Assets {
    private val entityAssociationMapper = Kemp.world.getMapper(EntityAssociationComponent::class.java)
    private val transformMapper = Kemp.world.getMapper(TransformComponent::class.java)
    private val nodeMapper = Kemp.world.getMapper(NodeComponent::class.java)

    override suspend fun loadModel(path: String, fileName: String): Model? = withContext(Dispatchers.IO) {
        //val inputStream = File(context.filesDir, "$path$fileSeparator$fileName").inputStream()
        val buffer = bufferForFile(path, fileName)
        val asset = assetLoader.createAssetFromBinary(buffer)
        return@withContext asset?.let { ast ->
            withContext(Dispatchers.Main) {
                resourceLoader.asyncBeginLoad(ast)
                while (resourceLoader.asyncGetLoadProgress() != 1f) {
                    resourceLoader.asyncUpdateLoad()
                }
            }

            ast.releaseSourceData()

            val model = AndroidModel(ast)

            val entities = model.entities()
            entities.forEach { implementationEntity ->
                val kempEntity = Kemp.world.create()
                val entityAssociation = entityAssociationMapper.create(kempEntity)
                entityAssociation.implementationEntity = implementationEntity
                transformMapper.create(kempEntity)
                val node = nodeMapper.create(kempEntity)
                val nodeName = model.nameOf(implementationEntity)
                node.name = nodeName
            }

            model
        }
    }

    override suspend fun loadIndirectLight(path: String, fileName: String): ImageBasedLighting = withContext(Dispatchers.IO) {
        val buffer = bufferForFile(path, fileName)
        val indirectLight = KtxLoader.createIndirectLight(engine, buffer)
        return@withContext AndroidImageBasedLighting(indirectLight)
    }

    private fun bufferForFile(path: String, fileName: String): ByteBuffer {
        val inputStream = context.assets.open("$path$fileSeparator$fileName")
        return inputStream.use { input ->
            val bytes = ByteArray(input.available())
            input.read(bytes)
            ByteBuffer.wrap(bytes)
        }
    }

    private fun transformToUnitCube(asset: FilamentAsset?, centerPoint: Float3 = Float3(0f, 0f, 0f)) {
        asset?.let { ast ->
            val tm = engine.transformManager
            var center = ast.boundingBox.center.let { v-> Float3(v[0], v[1], v[2]) }
            val halfExtent = ast.boundingBox.halfExtent.let { v-> Float3(v[0], v[1], v[2]) }
            val maxExtent = 2.0f * max(halfExtent)
            val scaleFactor = 2.0f / maxExtent
            center -= centerPoint / scaleFactor
            val transform = scale(Float3(scaleFactor)) * translation(-center)
            tm.setTransform(tm.getInstance(ast.root), transpose(transform).toFloatArray())
        }
    }
}