package com.kemp.core.config

/**
 * Options to control Depth of Field (DoF) effect in the scene
 *
 */
class DepthOfFieldOptions {
    /** focus distance in world units  */
    var focusDistance = 10.0f

    /** scale factor controlling the amount of blur (values other than 1.0 are not physically correct) */
    var blurScale = 1.0f

    /** maximum aperture diameter in meters (zero to disable bokeh rotation)  */
    var maxApertureDiameter = 0.01f

    /** enable or disable Depth of field effect  */
    var enabled = false
}