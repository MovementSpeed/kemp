package com.kemp.core.config

/**
 * Dynamic resolution can be used to either reach a desired target frame rate by lowering the
 * resolution of a `View`, or to increase the quality when the rendering is faster
 * than the target frame rate.
 *
 *
 *
 * This structure can be used to specify the minimum scale factor used when lowering the
 * resolution of a `View`, and the maximum scale factor used when increasing the
 * resolution for higher quality rendering. The scale factors can be controlled on each X and Y
 * axis independently. By default, all scale factors are set to 1.0.
 *
 *
 *
 *
 * Dynamic resolution is only supported on platforms where the time to render a frame can be
 * measured accurately. Dynamic resolution is currently only supported on Android.
 *
 */
class DynamicResolutionOptions {
    /**
     * Enables or disables dynamic resolution on a View.
     */
    var enabled = false

    /**
     * If false, the system scales the major axis first.
     */
    var homogeneousScaling = false

    /**
     * The minimum scale in X and Y this View should use.
     */
    var minScale = 0.5f

    /**
     * The maximum scale in X and Y this View should use.
     */
    var maxScale = 1.0f

    /**
     * Upscaling quality. LOW: 1 bilinear tap, MEDIUM: 4 bilinear taps, HIGH: 9 bilinear taps.
     * If minScale needs to be very low, it might help to use MEDIUM or HIGH here.
     * The default upscaling quality is set to LOW.
     */
    var quality = QualityLevel.LOW
}