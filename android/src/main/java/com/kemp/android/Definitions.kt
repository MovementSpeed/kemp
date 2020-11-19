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
import com.kemp.core.ecs.systems.ModelAnimationSystem
import com.kemp.core.ecs.systems.PhysicsSystem
import com.kemp.core.ecs.systems.RigidBodySystem
import com.kemp.core.ecs.systems.TouchElementsSystem
import com.kemp.core.physics.Physics
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
typealias FilamentView = View

val fileSeparator = File.separatorChar

fun androidCreate(context: Context, lifecycle: Lifecycle, game: Game): SurfaceView {
    val physicsSystem = PhysicsSystem()
    val physics = Physics(physicsSystem.physicsWorld, physicsSystem.space)

    val androidApplication = AndroidApplication(context) { _, worldConfig ->
        worldConfig.with(TouchElementsSystem())
        worldConfig.with(AndroidTouchUiRenderingSystem())
        worldConfig.with(ModelAnimationSystem())
        worldConfig.with(physicsSystem)
        worldConfig.with(RigidBodySystem())

        game.worldConfig(worldConfig)
    }

    val keyboardInput = AndroidKeyboardInput()
    val touchInput = AndroidTouchInput(androidApplication.view)

    val androidAssets = AndroidAssets(
        context,
        androidApplication.engine,
        androidApplication.assetLoader,
        androidApplication.resourceLoader)

    val androidScene = AndroidScene(androidApplication.engine, androidApplication.scene, androidApplication.filamentView)

    lifecycle.addObserver(androidApplication)

    val renderer2D = AndroidRenderer2D()
    androidApplication.view.addRenderDelegate(renderer2D)

    Kemp.application = androidApplication
    Kemp.keyboardInput = keyboardInput
    Kemp.ui.renderer2D = renderer2D
    Kemp.touchInput = touchInput
    Kemp.assets = androidAssets
    Kemp.scene = androidScene
    Kemp.physics = physics
    Kemp.game = game
    Kemp.start()

    return androidApplication.view
}