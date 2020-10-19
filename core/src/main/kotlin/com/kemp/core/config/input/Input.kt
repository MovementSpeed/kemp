package com.kemp.core.config.input

import com.kemp.core.InputTarget

data class Input(
    val type: InputType,
    val gamepadTargets: List<InputTarget>,
    val touchTargets: List<InputTarget>,
    val keyboardTargets: List<InputTarget>
)