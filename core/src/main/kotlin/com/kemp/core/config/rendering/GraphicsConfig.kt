package com.kemp.core.config.rendering

import com.kemp.core.utils.Color

class GraphicsConfig {
    var configChanged: () -> Unit = {}

    var width = 0
    var height = 0
    var shadowsEnabled = true
    var postProcessing = true
    var dithering = Dithering.NONE
    var antiAliasing = AntiAliasing.NONE
    val clearColor = Color(0, 0, 0, 255)
    val fogOptions = FogOptions()
    val bloomOptions = BloomOptions()
    val vignetteOptions = VignetteOptions()
    val colorGradingOptions = ColorGradingOptions()
    val depthOfFieldOptions = DepthOfFieldOptions()
    val ambientOcclusionOptions = AmbientOcclusionOptions()
    val dynamicResolutionOptions = DynamicResolutionOptions()
}