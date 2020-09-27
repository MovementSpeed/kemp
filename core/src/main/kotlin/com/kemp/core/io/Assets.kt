package com.kemp.core.io

import com.kemp.core.models.Model
import com.kemp.core.rendering.Environment
import com.kemp.core.rendering.ImageBasedLighting

interface Assets {
    suspend fun loadModel(path: String, fileName: String): Model?
    suspend fun loadIndirectLight(path: String, fileName: String): ImageBasedLighting
    suspend fun loadSkybox(path: String, fileName: String): Environment
}