package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.Seconds
import com.kemp.core.rendering.models.ModelAnimator
import com.kemp.core.rendering.models.animation.AnimationTake

class ModelAnimatorComponent : Component() {
    var modelAnimator: ModelAnimator? = null
    //var currentTime: Seconds = 0f
    var playbackSpeed = 1f
    var frameDuration = 1f / 30f

    var currentTake: AnimationTake? = null
    set(value) {
        field = value
        field?.currentTime = 0f
    }
    var takes: List<AnimationTake> = listOf()
}