package com.kemp.core.playground

import com.kemp.core.Kemp
import com.kemp.core.app.Game
import com.kemp.core.scene.Scene
import kotlinx.coroutines.launch

class PlaygroundGame : Game {
    override fun ready(scene: Scene) {
        Kemp.coroutineScope.launch {
            val model = Kemp.assets.loadModel("models", "model.glb")
            model?.let { scene.addEntities(model.entities()) }
        }
    }

    override fun update(delta: Float) {
    }
}