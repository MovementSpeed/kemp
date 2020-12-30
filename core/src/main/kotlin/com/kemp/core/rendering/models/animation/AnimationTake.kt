package com.kemp.core.rendering.models.animation

data class AnimationTake(val name: String, val frameStart: Int, val frameEnd: Int) {
    var currentTime = 0f
}
