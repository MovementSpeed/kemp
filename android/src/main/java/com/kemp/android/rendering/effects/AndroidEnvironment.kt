package com.kemp.android.rendering.effects

import com.google.android.filament.Engine
import com.google.android.filament.Skybox
import com.kemp.core.rendering.effects.Environment
import com.kemp.core.utils.Color

class AndroidEnvironment(private val engine: Engine, val skybox: Skybox) : Environment {
    override fun color(r: Float, g: Float, b: Float, a: Float) {
        skybox.setColor(r, g, b, a)
    }

    override fun color(c: Color) {
        skybox.setColor(c.array)
    }

    override fun intensity(): Float {
        return skybox.intensity
    }

    override fun dispose() {
        engine.destroySkybox(skybox)
    }
}