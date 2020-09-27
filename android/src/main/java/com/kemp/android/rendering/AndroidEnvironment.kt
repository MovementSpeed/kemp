package com.kemp.android.rendering

import com.google.android.filament.Skybox
import com.kemp.core.rendering.Environment
import com.kemp.core.utils.Color

class AndroidEnvironment(val skybox: Skybox) : Environment {
    override fun color(r: Float, g: Float, b: Float, a: Float) {
        skybox.setColor(r, g, b, a)
    }

    override fun color(c: Color) {
        skybox.setColor(c.array)
    }

    override fun intensity(): Float {
        return skybox.intensity
    }
}