package com.kemp.core.config.rendering

import com.kemp.core.utils.Color

/**
 * Options to control fog in the scene
 *
 */
class FogOptions {
    /**
     * distance in world units from the camera where the fog starts ( >= 0.0 )
     */
    var distance = 0.0f

    /**
     * fog's maximum opacity between 0 and 1
     */
    var maximumOpacity = 1.0f

    /**
     * fog's floor in world units
     */
    var height = 0.0f

    /**
     * how fast fog dissipates with altitude
     */
    var heightFalloff = 1.0f

    /**
     * Fog's color as a linear RGB color.
     */
    var color = Color(127, 127, 127)

    /**
     * fog's density at altitude given by 'height'
     */
    var density = 0.1f

    /**
     * distance in world units from the camera where in-scattering starts
     */
    var inScatteringStart = 0.0f

    /**
     * size of in-scattering (>=0 to activate). Good values are >> 1 (e.g. ~10 - 100)
     */
    var inScatteringSize = 0.0f

    /**
     * fog color will be modulated by the IBL color in the view direction
     */
    var fogColorFromIbl = false

    /**
     * enable or disable fog
     */
    var enabled = false
}