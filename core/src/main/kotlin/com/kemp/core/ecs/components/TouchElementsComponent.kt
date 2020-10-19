package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.InputTarget
import com.kemp.core.input.touch.TouchElement

class TouchElementsComponent : Component() {
    val touchElements = mutableMapOf<InputTarget, TouchElement>()
}