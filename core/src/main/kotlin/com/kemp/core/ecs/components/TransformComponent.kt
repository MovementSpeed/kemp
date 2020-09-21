package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.scene.Transform

class TransformComponent : Component() {
    val transform: Transform = Transform()
}