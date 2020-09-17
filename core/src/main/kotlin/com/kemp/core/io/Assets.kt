package com.kemp.core.io

import com.kemp.core.models.Model

interface Assets {
    suspend fun loadModel(path: String, fileName: String): Model?
}