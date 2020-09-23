package com.kemp.core.app

import com.artemis.WorldConfigurationBuilder
import com.kemp.core.scene.Scene

interface Game {
    fun worldConfig(worldConfigurationBuilder: WorldConfigurationBuilder) {}
    fun ready(scene: Scene)
    fun update(delta: Float)
}