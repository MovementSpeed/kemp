package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.input.touch.TouchStick

class TouchStickComponent : Component() {
    var enabled = true
    lateinit var touchStick: TouchStick
}