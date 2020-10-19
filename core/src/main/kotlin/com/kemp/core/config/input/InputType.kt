package com.kemp.core.config.input

import com.kemp.core.utils.Float2

sealed class InputType
class Vectorial : InputType() {
    val vec = Float2()
}