package com.kemp.android

import android.content.Context
import android.view.SurfaceView
import androidx.lifecycle.Lifecycle
import com.google.android.filament.*
import com.kemp.android.app.AndroidApplication
import com.kemp.android.ecs.systems.AndroidTouchUiRenderingSystem
import com.kemp.android.input.AndroidKeyboardInput
import com.kemp.android.input.AndroidTouchInput
import com.kemp.android.io.AndroidAssets
import com.kemp.android.rendering.ui.AndroidRenderer2D
import com.kemp.android.scene.AndroidScene
import com.kemp.core.Kemp
import com.kemp.core.app.Game
import com.kemp.core.ecs.systems.TouchElementsSystem
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

fun androidCreate(context: Context, lifecycle: Lifecycle, game: Game): SurfaceView {
    val androidApplication = AndroidApplication(context) { app, worldConfig ->
        worldConfig.with(TouchElementsSystem())
        worldConfig.with(AndroidTouchUiRenderingSystem())

        game.worldConfig(worldConfig)
    }

    val keyboardInput = AndroidKeyboardInput()
    val touchInput = AndroidTouchInput(androidApplication.view)

    val androidAssets = AndroidAssets(
        context,
        androidApplication.engine,
        androidApplication.assetLoader,
        androidApplication.resourceLoader)

    val androidScene = AndroidScene(androidApplication.scene, androidApplication.ecsCameraEntity)

    lifecycle.addObserver(androidApplication)

    val renderer2D = AndroidRenderer2D()
    androidApplication.view.addRenderDelegate(renderer2D)

    Kemp.application = androidApplication
    Kemp.ui.renderer2D = renderer2D
    Kemp.touchInput = touchInput
    Kemp.keyboardInput = keyboardInput
    Kemp.assets = androidAssets
    Kemp.scene = androidScene
    Kemp.game = game
    Kemp.start()

    return androidApplication.view
}