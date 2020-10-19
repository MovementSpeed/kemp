package com.kemp.core.config.rendering

import com.kemp.core.utils.Color

/**
 * Options to control the vignetting effect.
 */
class VignetteOptions {
    /**
     * High values restrict the vignette closer to the corners, between 0 and 1.
     */
    var midPoint = 0.5f

    /**
     * Controls the shape of the vignette, from a rounded rectangle (0.0), to an oval (0.5),
     * to a circle (1.0). The value must be between 0 and 1.
     */
    var roundness = 0.5f

    /**
     * Softening amount of the vignette effect, between 0 and 1.
     */
    var feather = 0.5f

    /**
     * Color of the vignette effect as a linear RGBA color. The alpha channel is currently
     * ignored.
     */
    var color = Color(0, 0, 0, 255)

    /**
     * Enables or disables the vignette effect.
     */
    var enabled = false
}