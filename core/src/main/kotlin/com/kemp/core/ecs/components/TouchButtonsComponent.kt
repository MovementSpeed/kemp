package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.Name
import com.kemp.core.input.touch.TouchButton
import com.kemp.core.input.touch.TouchStick

class TouchButtonsComponent : Component() {
    val touchButtons = mutableMapOf<Name, TouchButton>()
}