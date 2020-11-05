package com.kemp.core.io

import com.kemp.core.audio.Music
import com.kemp.core.audio.Sound
import com.kemp.core.config.input.InputConfiguration
import com.kemp.core.interfaces.Disposable
import com.kemp.core.rendering.models.Model
import com.kemp.core.rendering.effects.Environment
import com.kemp.core.rendering.effects.ImageBasedLighting

interface Assets : Disposable {
    suspend fun loadModel(path: String, fileName: String): Model
    suspend fun loadIndirectLight(path: String, fileName: String): ImageBasedLighting
    suspend fun loadSkybox(path: String, fileName: String): Environment
    suspend fun loadSound(path: String, fileName: String): Sound?
    suspend fun loadMusic(path: String, fileName: String): Music?
    suspend fun loadInputConfiguration(path: String, fileName: String): InputConfiguration
}