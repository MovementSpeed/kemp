package com.kemp.core.config.rendering

import com.kemp.core.utils.Color

data class ColorGradingOptions(
    val quality: QualityLevel = QualityLevel.MEDIUM,
    val toneMapping: ToneMapping = ToneMapping.ACES_LEGACY,
    val whiteBalanceTemperature: Float = 0f,
    val whiteBalanceTint: Float = 0f,
    val channelMixerRed: Color = Color(255, 0, 0),
    val channelMixerGreen: Color = Color(0, 255, 0),
    val channelMixerBlue: Color = Color(0, 0, 255),
    val contrast: Float = 1f,
    val vibrance: Float = 1f,
    val saturation: Float = 1f
)