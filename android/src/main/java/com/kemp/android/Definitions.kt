package com.kemp.android

import com.google.android.filament.*
import java.io.File

typealias AttachStateListener = android.view.View.OnAttachStateChangeListener
typealias FilamentMaterialInstance = MaterialInstance
typealias FilamentTexture = Texture
typealias FilamentTextureSampler = TextureSampler
typealias FilamentBooleanElement = MaterialInstance.BooleanElement
typealias FilamentIntElement = MaterialInstance.IntElement
typealias FilamentRgbType = Colors.RgbType
typealias FilamentRgbaType = Colors.RgbaType
typealias FilamentCullingMode = Material.CullingMode
typealias FilamentScene = Scene

val fileSeparator = File.separatorChar