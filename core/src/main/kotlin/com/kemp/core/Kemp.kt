package com.kemp.core

import com.artemis.World
import com.kemp.core.app.Application
import com.kemp.core.app.Game
import com.kemp.core.config.GraphicsConfig
import com.kemp.core.input.TouchInput
import com.kemp.core.io.Assets
import com.kemp.core.scene.Scene
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Kemp {
    val graphicsConfig = GraphicsConfig()
    val coroutineScope = CoroutineScope(Dispatchers.Default)

    lateinit var application: Application
    lateinit var assets: Assets
    lateinit var scene: Scene
    lateinit var game: Game
    lateinit var world: World
    lateinit var touchInput: TouchInput

    fun start() {
        graphicsConfig.configChanged = {
            application.graphicsConfigChanged(graphicsConfig)
        }

        game.ready(scene)
        application.update = { frameTimeNanos ->
            // Update engine systems
            world.process()

            // Update current game
            game.update(0f)

            // Update
            touchInput.update()
        }
    }
}