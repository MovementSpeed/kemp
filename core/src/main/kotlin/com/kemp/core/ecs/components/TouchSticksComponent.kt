package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.Name
import com.kemp.core.input.touch.TouchStick

class TouchSticksComponent : Component() {
    val touchSticks = mutableMapOf<Name, TouchStick>()
}