package com.kemp.android.io

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.google.android.filament.Engine
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.ResourceLoader
import com.google.android.filament.utils.*
import com.kemp.android.audio.AndroidMusic
import com.kemp.android.audio.AndroidSound
import com.kemp.android.fileSeparator
import com.kemp.android.maxAudioStreams
import com.kemp.android.rendering.models.AndroidModel
import com.kemp.android.rendering.effects.AndroidEnvironment
import com.kemp.android.rendering.effects.AndroidImageBasedLighting
import com.kemp.android.suspendLoad
import com.kemp.core.Entity
import com.kemp.core.Kemp
import com.kemp.core.audio.Music
import com.kemp.core.audio.Sound
import com.kemp.core.config.input.InputConfiguration
import com.kemp.core.ecs.components.EntityAssociationComponent
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.interfaces.Disposable
import com.kemp.core.io.Assets
import com.kemp.core.mapper
import com.kemp.core.rendering.models.Model
import com.kemp.core.rendering.effects.Environment
import com.kemp.core.rendering.effects.ImageBasedLighting
import com.kemp.core.utils.Mat4
import com.kemp.core.utils.transposeFast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer

class AndroidAssets(
    private val context: Context,
    private val engine: Engine,
    private val assetLoader: AssetLoader,
    private val resourceLoader: ResourceLoader
) : Assets {
    private val entityAssociationMapper = mapper<EntityAssociationComponent>()
    private val transformMapper = mapper<TransformComponent>()
    private val nodeMapper = mapper<NodeComponent>()

    private val disposables = mutableListOf<Disposable>()

    private val helperMediaPlayer = MediaPlayer()
    private val soundPool = SoundPool.Builder()
        .run {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            setAudioAttributes(audioAttributes)
            setMaxStreams(maxAudioStreams)
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

                val model = AndroidModel(assetLoader, ast)
                val root = model.root()
                val name = fileName.takeWhile { it != '.' }
                val entity = setupModelEntity(model, true, name, root)
                model.entity = entity

                val entities = model.entities()
                entities.forEach { implementationEntity ->
                    setupModelEntity(model, false, name, implementationEntity)
                }

                disposables.add(model)
                model
            }
        }

    override suspend fun loadIndirectLight(path: String, fileName: String): ImageBasedLighting =
        withContext(Dispatchers.Default) {
            val buffer = bufferForFile(path, fileName)
            val indirectLight = KtxLoader.createIndirectLight(engine, buffer)
            val imageBasedLighting = AndroidImageBasedLighting(engine, indirectLight)

            disposables.add(imageBasedLighting)
            return@withContext imageBasedLighting
        }

    override suspend fun loadSkybox(path: String, fileName: String): Environment =
        withContext(Dispatchers.Default) {
            val buffer = bufferForFile(path, fileName)
            val skybox = KtxLoader.createSkybox(engine, buffer)
            val environment = AndroidEnvironment(engine, skybox)

            disposables.add(environment)
            return@withContext environment
        }

    override suspend fun loadSound(path: String, fileName: String): Sound? =
        withContext(Dispatchers.Default) {
            val soundFileAsync = async(Dispatchers.IO) {
                context.assets.openFd("$path$fileSeparator$fileName")
            }

            val soundFile = soundFileAsync.await()
            helperMediaPlayer.reset()

            val durationAsync = async(Dispatchers.IO) {
                helperMediaPlayer.setDataSource(soundFile)
                helperMediaPlayer.prepare()
                helperMediaPlayer.duration
            }

            val duration = durationAsync.await()

            val soundId = soundPool.suspendLoad(soundFile)
            val sound = AndroidSound(soundPool, soundId, duration)

            disposables.add(sound)
            return@withContext sound
        }

    override suspend fun loadMusic(path: String, fileName: String): Music? =
        withContext(Dispatchers.Default) {
            val musicFileAsync = async(Dispatchers.IO) {
                context.assets.openFd("$path$fileSeparator$fileName")
            }

            val musicFile = musicFileAsync.await()
            val mediaPlayer = MediaPlayer()

            val prepareAsync = async(Dispatchers.IO) {
                mediaPlayer.setDataSource(musicFile)
                mediaPlayer.prepare()
            }

            prepareAsync.await()
            val music = AndroidMusic(mediaPlayer)

            disposables.add(music)
            return@withContext music
        }

    override suspend fun loadInputConfiguration(path: String, fileName: String): InputConfiguration {
        return InputConfiguration()
    }

    override fun dispose() {
        disposables.forEach { it.dispose() }
        helperMediaPlayer.release()
        soundPool.release()
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