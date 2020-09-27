package com.kemp.core.config

class AmbientOcclusionOptions {
    var enabled: Boolean = false

    /**
     * Ambient Occlusion radius in meters, between 0 and ~10.
     */
    var radius = 0.3f

    /**
     * Self-occlusion bias in meters. Use to avoid self-occlusion. Between 0 and a few mm.
     */
    var bias = 0.0005f

    /**
     * Controls ambient occlusion's contrast. Must be positive. Default is 1.
     * Good values are between 0.5 and 3.
     */
    var power = 1.0f

    /**
     * How each dimension of the AO buffer is scaled. Must be either 0.5 or 1.0.
     */
    var resolution = 0.5f

    /**
     * Strength of the Ambient Occlusion effect. Must be positive.
     */
    var intensity = 1.0f

    /**
     * The quality setting controls the number of samples used for evaluating Ambient
     * occlusion. The default is QualityLevel.LOW which is sufficient for most mobile
     * applications.
     */
    var quality = QualityLevel.LOW

    /**
     * The upsampling setting controls the quality of the ambient occlusion buffer upsampling.
     * The default is QualityLevel.LOW and uses bilinear filtering, a value of
     * QualityLevel.HIGH or more enables a better bilateral filter.
     */
    var upsampling = QualityLevel.LOW
}