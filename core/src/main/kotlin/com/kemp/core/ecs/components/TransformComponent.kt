package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.scene.Transform

data class TransformComponent(val transform: Transform = Transform()) : Component()