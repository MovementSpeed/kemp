package com.kemp.android.io

import android.content.Context
import com.google.android.filament.Engine
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.FilamentAsset
import com.google.android.filament.gltfio.ResourceLoader
import com.google.android.filament.utils.*
import com.kemp.android.fileSeparator
import com.kemp.android.models.AndroidModel
import com.kemp.core.io.Assets
import com.kemp.core.models.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.ByteBuffer

class AndroidAssets(private val context: Context,
                    private val engine: Engine,
                    private val assetLoader: AssetLoader,
                    private val resourceLoader: ResourceLoader) : Assets {
    override suspend fun loadModel(path: String, fileName: String): Model? = withContext(Dispatchers.IO) {
        //val inputStream = File(context.filesDir, "$path$fileSeparator$fileName").inputStream()

        val inputStream = context.assets.open("$path$fileSeparator$fileName")
        val buffer = inputStream.use { input ->
            val bytes = ByteArray(input.available())
            input.read(bytes)
            ByteBuffer.wrap(bytes)
        }

        val asset = assetLoader.createAssetFromBinary(buffer)
        return@withContext asset?.let { ast ->
            withContext(Dispatchers.Main) {
                resourceLoader.asyncBeginLoad(ast)
                while (resourceLoader.asyncGetLoadProgress() != 1f) {
                    resourceLoader.asyncUpdateLoad()
                }
            }

            ast.releaseSourceData()
            transformToUnitCube(ast)
            AndroidModel(ast)
        }
    }

    fun transformToUnitCube(asset: FilamentAsset?, centerPoint: Float3 = Float3(0f, 0f, -4f)) {
        asset?.let { asset ->
            val tm = engine.transformManager
            var center = asset.boundingBox.center.let { v-> Float3(v[0], v[1], v[2]) }
            val halfExtent = asset.boundingBox.halfExtent.let { v-> Float3(v[0], v[1], v[2]) }
            val maxExtent = 2.0f * max(halfExtent)
            val scaleFactor = 2.0f / maxExtent
            center -= centerPoint / scaleFactor
            val transform = scale(Float3(scaleFactor)) * translation(-center)
            tm.setTransform(tm.getInstance(asset.root), transpose(transform).toFloatArray())
        }
    }
}