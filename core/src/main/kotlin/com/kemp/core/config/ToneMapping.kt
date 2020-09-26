package com.kemp.core.config

enum class ToneMapping {
    /** Linear tone mapping (i.e. no tone mapping).  */
    LINEAR,

    /** ACES tone mapping, with a brightness modifier to match Filament's legacy tone mapper.  */
    ACES_LEGACY,

    /** ACES tone mapping.  */
    ACES,

    /** Filmic tone mapping, modelled after ACES but applied in sRGB space.  */
    FILMIC,

    /** Filmic tone mapping, with more contrast and saturation.  */
    UCHIMURA,

    /** Reinhard luma-based tone mapping.  */
    REINHARD,

    /** Tone mapping used to validate/debug scene exposure.  */
    DISPLAY_RANGE
}