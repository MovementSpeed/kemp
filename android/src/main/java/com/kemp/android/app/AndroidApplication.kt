package com.kemp.android.app

import android.content.Context
import android.view.Choreographer
import android.view.Surface
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.artemis.ComponentMapper
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.google.android.filament.*
import com.google.android.filament.android.DisplayHelper
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.MaterialProvider
import com.google.android.filament.gltfio.ResourceLoader
import com.google.android.filament.utils.*
import com.kemp.android.AttachStateListener
import com.kemp.android.ecs.systems.AndroidTransformSystem
import com.kemp.core.Entity
import com.kemp.core.Kemp
import com.kemp.core.app.Application
import com.kemp.core.ecs.components.CameraNodeComponent
import com.kemp.core.ecs.components.EntityAssociationComponent
import com.kemp.core.ecs.components.TransformComponent

class AndroidApplication(private val context: Context) : Application, UiHelper.RendererCallback, LifecycleObserver,
    AttachStateListener {
    override var update: (frameTimeNanos: Long) -> Unit = {}

    lateinit var engine: Engine
    lateinit var view: SurfaceView
    lateinit var assetLoader: AssetLoader
    lateinit var resourceLoader: ResourceLoader
    lateinit var scene: Scene

    // Ecs
    private lateinit var entityAssociationMapper: ComponentMapper<EntityAssociationComponent>
    private lateinit var transformMapper: ComponentMapper<TransformComponent>
    private lateinit var cameraNodeMapper: ComponentMapper<CameraNodeComponent>

    // Android rendering framework
    private lateinit var choreographer: Choreographer
    private val frameScheduler = Choreographer.FrameCallback(::androidFrame)

    // Filament helpers
    private lateinit var uiHelper: UiHelper
    private lateinit var displayHelper: DisplayHelper

    // Filament apis
    private var swapChain: SwapChain? = null

    private lateinit var renderer: Renderer
    private lateinit var filamentView: View

    var cameraEntity: Entity = -1
    private lateinit var camera: Camera

    init {
        Utils.init()
        initFilament()
        initEcs()
        setupCamera()
        initView()
    }

    // UiHelper.RendererCallback
    override fun onNativeWindowChanged(surface: Surface) {
        swapChain?.let { sc -> engine.destroySwapChain(sc) }
        swapChain = engine.createSwapChain(surface)

        displayHelper.attach(renderer, view.display)
    }

    override fun onDetachedFromSurface() {
        displayHelper.detach()
        swapChain?.let { sc ->
            engine.destroySwapChain(sc)
            engine.flushAndWait()
            swapChain = null
        }
    }

    override fun onResized(width: Int, height: Int) {
        filamentView.viewport = Viewport(0, 0, width, height)

        val aspect = width.toDouble() / height.toDouble()
        camera.setProjection(45.0, aspect, 0.01, 1000.0, Camera.Fov.VERTICAL)
    }

    override fun onViewAttachedToWindow(v: android.view.View?) {
        // Nothing
    }

    override fun onViewDetachedFromWindow(v: android.view.View?) {
        destroyFilament()
    }

    // Android
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun paused() {
        choreographer.removeFrameCallback(frameScheduler)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumed() {
        choreographer.postFrameCallback(frameScheduler)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroyed() {
        choreographer.removeFrameCallback(frameScheduler)
        destroyFilament()
    }

    private fun androidFrame(frameTimeNanos: Long) {
        Kemp.world.process()
        update(frameTimeNanos)
        choreographer.postFrameCallback(frameScheduler)

        if (!uiHelper.isReadyToRender) return
        val sc = swapChain ?: return

        if (renderer.beginFrame(sc, frameTimeNanos)) {
            renderer.render(filamentView)
            renderer.endFrame()
        }
    }

    private fun initEcs() {
        val worldConf = WorldConfigurationBuilder()
            .with(AndroidTransformSystem(engine, engine.transformManager))
            .build()

        Kemp.world = World(worldConf)
        entityAssociationMapper = Kemp.world.getMapper(EntityAssociationComponent::class.java)
        transformMapper = Kemp.world.getMapper(TransformComponent::class.java)
        cameraNodeMapper = Kemp.world.getMapper(CameraNodeComponent::class.java)
    }

    // Filament
    private fun initFilament() {
        engine = Engine.create()
        renderer = engine.createRenderer()
        scene = engine.createScene()
        filamentView = engine.createView()

        val co = Renderer.ClearOptions()
        co.clear = true
        co.clearColor = floatArrayOf(1f, 0f, 0f, 1f)
        renderer.clearOptions = co

        filamentView.scene = scene

        assetLoader = AssetLoader(engine, MaterialProvider(engine), EntityManager.get())
        resourceLoader = ResourceLoader(engine, true, false)

        val light = EntityManager.get().create()

        val (r, g, b) = Colors.cct(6_500.0f)
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(r, g, b)
            .intensity(100_000.0f)
            .direction(0.0f, -1.0f, 0.0f)
            .castShadows(true)
            .build(engine, light)

        scene.addEntity(light)
    }

    private fun initView() {
        view = SurfaceView(context)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        choreographer = Choreographer.getInstance()
        displayHelper = DisplayHelper(context)

        uiHelper = UiHelper(UiHelper.ContextErrorPolicy.CHECK)
        uiHelper.renderCallback = this

        uiHelper.attachTo(view)

        view.addOnAttachStateChangeListener(this)
    }

    private fun destroyFilament() {
        uiHelper.detach()

        assetLoader.destroy()
        resourceLoader.destroy()

        engine.destroyRenderer(renderer)
        engine.destroyView(filamentView)
        engine.destroyScene(scene)
        engine.destroyCamera(camera)

        engine.destroy()
    }

    private fun setupCamera() {
        val em = EntityManager.get()
        val cameraEntity = em.create()
        camera = engine.createCamera(cameraEntity)
        filamentView.camera = camera

        val kempCameraEntity = Kemp.world.create()
        val entityAssociation = entityAssociationMapper.create(kempCameraEntity)
        entityAssociation.implementationEntity = cameraEntity
        transformMapper.create(kempCameraEntity)
        cameraNodeMapper.create(kempCameraEntity)

        camera.setExposure(16f, 1f / 125f, 100f)

        val mat = rotation(Float3(0f, 1f, 0f), 180f) * translation(Float3(0f, 0f, 0f))
        val position = mat.position
        val forward = mat.forward
        val up = mat.up

        camera.lookAt(
            position.x.toDouble(),
            position.y.toDouble(),
            position.z.toDouble(),

            forward.x.toDouble(),
            forward.y.toDouble(),
            forward.z.toDouble(),

            up.x.toDouble(),
            up.y.toDouble(),
            up.z.toDouble()
        )
    }
}