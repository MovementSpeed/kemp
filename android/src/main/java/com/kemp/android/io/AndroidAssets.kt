package com.kemp.android.io

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.google.android.filament.Engine
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.FilamentAsset
import com.google.android.filament.gltfio.ResourceLoader
import com.google.android.filament.utils.*
import com.kemp.android.audio.AndroidSound
import com.kemp.android.fileSeparator
import com.kemp.android.models.AndroidModel
import com.kemp.android.rendering.AndroidEnvironment
import com.kemp.android.rendering.AndroidImageBasedLighting
import com.kemp.android.suspendLoad
import com.kemp.core.Entity
import com.kemp.core.Kemp
import com.kemp.core.audio.Sound
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
    private val soundPool = SoundPool.Builder()
        .run {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            setAudioAttributes(audioAttributes)
            setMaxStreams(16)
            build()
        }

    override suspend fun loadModel(path: String, fileName: String): Model? =
        withContext(Dispatchers.Default) {
            val buffer = bufferForFile(path, fileName)
            val asset = assetLoader.createAssetFromBinary(buffer)
            return@withContext asset?.let { ast ->
                withContext(Dispatchers.Main) {
                    resourceLoader.loadResources(ast)
                }

                ast.releaseSourceData()

                val model = AndroidModel(ast)
                val root = model.root()
                val name = fileName.takeWhile { it != '.' }
                val entity = setupModelEntity(model, true, name, root)
                model.entity = entity

                val entities = model.entities()
                entities.forEach { implementationEntity ->
                    setupModelEntity(model, false, name, implementationEntity)
                }

                model
            }
        }

    override suspend fun loadIndirectLight(path: String, fileName: String): ImageBasedLighting =
        withContext(Dispatchers.Default) {
            val buffer = bufferForFile(path, fileName)
            val indirectLight = KtxLoader.createIndirectLight(engine, buffer)
            return@withContext AndroidImageBasedLighting(indirectLight)
        }

    override suspend fun loadSkybox(path: String, fileName: String): Environment =
        withContext(Dispatchers.Default) {
            val buffer = bufferForFile(path, fileName)
            val skybox = KtxLoader.createSkybox(engine, buffer)
            return@withContext AndroidEnvironment(skybox)
        }

    override suspend fun loadSound(path: String, fileName: String): Sound? =
        withContext(Dispatchers.Default) {
            val soundFile = context.assets.openFd("$path$fileSeparator$fileName")
            val soundId = soundPool.suspendLoad(soundFile)
            return@withContext AndroidSound(soundPool, soundId)
        }

    private fun bufferForFile(path: String, fileName: String): ByteBuffer {
        val inputStream = context.assets.open("$path$fileSeparator$fileName")
        return inputStream.use { input ->
            val bytes = ByteArray(input.available())
            input.read(bytes)
            ByteBuffer.wrap(bytes)
        }
    }

    private fun setupModelEntity(model: Model, root: Boolean, baseName: String, implementationEntity: Int): Entity {
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

        return kempEntity
    }
}