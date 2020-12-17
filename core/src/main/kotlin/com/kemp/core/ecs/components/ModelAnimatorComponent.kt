package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.Seconds
import com.kemp.core.rendering.models.ModelAnimator

class ModelAnimatorComponent : Component() {
    var modelAnimator: ModelAnimator? = null
    var currentTime: Seconds = 0f
    var lastFrameTimestamp: Long = 0L
    var playbackSpeed = 1f
}