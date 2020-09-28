package com.kemp.android.io

import android.content.Context
import com.google.android.filament.Engine
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.FilamentAsset
import com.google.android.filament.gltfio.ResourceLoader
import com.google.android.filament.utils.*
import com.kemp.android.fileSeparator
import com.kemp.android.models.AndroidModel
import com.kemp.android.rendering.AndroidEnvironment
import com.kemp.android.rendering.AndroidImageBasedLighting
import com.kemp.core.Kemp
import com.kemp.core.ecs.components.EntityAssociationComponent
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.io.Assets
import com.kemp.core.models.Model
import com.kemp.core.rendering.Environment
import com.kemp.core.rendering.ImageBasedLighting
import com.kemp.core.utils.Mat4
import com.kemp.core.utils.transposeFast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer

class AndroidAssets(
    private val context: Context,
    private val engine: Engine,
    private val assetLoader: AssetLoader,
    private val resourceLoader: ResourceLoader
) : Assets {
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
            val root = model.root()
            setupModelEntity(model, true, fileName, root)

            val entities = model.entities()
            entities.forEach { implementationEntity ->
                setupModelEntity(model, false, fileName, implementationEntity)
            }

            model
        }
    }

    private fun setupModelEntity(model: Model, root: Boolean, baseName: String, implementationEntity: Int) {
        val kempEntity = Kemp.world.create()

        val entityAssociationComponent = entityAssociationMapper.create(kempEntity)
        entityAssociationComponent.implementationEntity = implementationEntity

        val transformComponent = transformMapper.create(kempEntity)

        val ieInstance = engine.transformManager.getInstance(implementationEntity)
        val ieTransform = engine.transformManager.getWorldTransform(ieInstance, null)
        transformComponent.transform.matrix = Mat4.of(*ieTransform)
        transposeFast(transformComponent.transform.matrix)
        transformComponent.transform.update()

        val nodeComponent = nodeMapper.create(kempEntity)
        val nodeName = if (!root) model.nameOf(implementationEntity) else "root"
        nodeComponent.name = "${baseName}_$nodeName"
    }

    override suspend fun loadIndirectLight(path: String, fileName: String): ImageBasedLighting =
        withContext(Dispatchers.IO) {
            val buffer = bufferForFile(path, fileName)
            val indirectLight = KtxLoader.createIndirectLight(engine, buffer)
            return@withContext AndroidImageBasedLighting(indirectLight)
        }

    override suspend fun loadSkybox(path: String, fileName: String): Environment = withContext(Dispatchers.IO) {
        val buffer = bufferForFile(path, fileName)
        val skybox = KtxLoader.createSkybox(engine, buffer)
        return@withContext AndroidEnvironment(skybox)
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
            var center = ast.boundingBox.center.let { v -> Float3(v[0], v[1], v[2]) }
            val halfExtent = ast.boundingBox.halfExtent.let { v -> Float3(v[0], v[1], v[2]) }
            val maxExtent = 2.0f * max(halfExtent)
            val scaleFactor = 2.0f / maxExtent
            center -= centerPoint / scaleFactor
            val transform = scale(Float3(scaleFactor)) * translation(-center)
            tm.setTransform(tm.getInstance(ast.root), transpose(transform).toFloatArray())
        }
    }
}