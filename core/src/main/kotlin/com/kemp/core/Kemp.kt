package com.kemp.core

import com.artemis.World
import com.kemp.core.app.Application
import com.kemp.core.app.Game
import com.kemp.core.io.Assets
import com.kemp.core.scene.Scene
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Kemp {
    val world = World()
    val coroutineScope = CoroutineScope(Dispatchers.Default)

    lateinit var application: Application
    lateinit var assets: Assets
    lateinit var scene: Scene
    lateinit var game: Game

    fun start() {
        game.ready(scene)
        application.update = { frameTimeNanos ->
            // Update engine systems

            // Update current game
            game.update(0f)
        }
    }
}