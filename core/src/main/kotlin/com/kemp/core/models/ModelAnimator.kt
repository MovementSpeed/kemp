package com.kemp.core.models

import com.kemp.core.Seconds

interface ModelAnimator {
    fun animate(animIndex: Int, time: Seconds)
    fun animationDuration(animIndex: Int): Seconds
    fun animationName(animIndex: Int): String
    fun update()
    fun animationsCount(): Int
}