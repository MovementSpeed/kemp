package com.kemp.core.app

import com.kemp.core.scene.Scene

interface Game {
    fun ready(scene: Scene)
    fun update(delta: Float)
}