package com.kemp.core.io

import com.kemp.core.audio.Music
import com.kemp.core.audio.Sound
import com.kemp.core.interfaces.Disposable
import com.kemp.core.models.Model
import com.kemp.core.rendering.Environment
import com.kemp.core.rendering.ImageBasedLighting

interface Assets : Disposable {
    suspend fun loadModel(path: String, fileName: String): Model?
    suspend fun loadIndirectLight(path: String, fileName: String): ImageBasedLighting
    suspend fun loadSkybox(path: String, fileName: String): Environment
    suspend fun loadSound(path: String, fileName: String): Sound?
    suspend fun loadMusic(path: String, fileName: String): Music?
}