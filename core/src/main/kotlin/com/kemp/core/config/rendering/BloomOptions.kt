package com.kemp.core.config.rendering

/**
 * Options for controlling the Bloom effect
 *
 * enabled:     Enable or disable the bloom post-processing effect. Disabled by default.
 * levels:      Number of successive blurs to achieve the blur effect, the minimum is 3 and the
 * maximum is 12. This value together with resolution influences the spread of the
 * blur effect. This value can be silently reduced to accommodate the original
 * image size.
 * resolution:  Resolution of bloom's minor axis. The minimum value is 2^levels and the
 * the maximum is lower of the original resolution and 4096. This parameter is
 * silently clamped to the minimum and maximum.
 * It is highly recommended that this value be smaller than the target resolution
 * after dynamic resolution is applied (horizontally and vertically).
 * strength:    how much of the bloom is added to the original image. Between 0 and 1.
 * blendMode:   Whether the bloom effect is purely additive (false) or mixed with the original
 * image (true).
 * anamorphism: Bloom's aspect ratio (x/y), for artistic purposes.
 * threshold:   When enabled, a threshold at 1.0 is applied on the source image, this is
 * useful for artistic reasons and is usually needed when a dirt texture is used.
 * dirt:        A dirt/scratch/smudges texture (that can be RGB), which gets added to the
 * bloom effect. Smudges are visible where bloom occurs. Threshold must be
 * enabled for the dirt effect to work properly.
 * dirtStrength: Strength of the dirt texture.
 *
 */
class BloomOptions {
    enum class BlendingMode {
        ADD, INTERPOLATE
    }

    /**
     * strength of the dirt texture
     */
    var dirtStrength = 0.2f

    /**
     * Strength of the bloom effect, between 0.0 and 1.0
     */
    var strength = 0.10f

    /**
     * Resolution of minor axis (2^levels to 4096)
     */
    var resolution = 360

    /**
     * Bloom x/y aspect-ratio (1/32 to 32)
     */
    var anamorphism = 1.0f

    /**
     * Number of blur levels (3 to 12)
     */
    var levels = 6

    /**
     * How the bloom effect is applied
     */
    var blendingMode = BlendingMode.ADD

    /**
     * Whether to threshold the source
     */
    var threshold = true

    /**
     * enable or disable bloom
     */
    var enabled = false
}