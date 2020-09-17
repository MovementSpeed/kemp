package com.kemp.android.models

import com.google.android.filament.gltfio.Animator
import com.kemp.core.Seconds
import com.kemp.core.models.ModelAnimator

class AndroidModelAnimator(private val modelAnimator: Animator) : ModelAnimator {
    override fun animate(animIndex: Int, time: Seconds) {
        modelAnimator.applyAnimation(animIndex, time)
    }

    override fun animationDuration(animIndex: Int): Seconds {
        return modelAnimator.getAnimationDuration(animIndex)
    }

    override fun animationName(animIndex: Int): String {
        return modelAnimator.getAnimationName(animIndex)
    }

    override fun update() {
        modelAnimator.updateBoneMatrices()
    }

    override fun animationsCount(): Int {
        return modelAnimator.animationCount
    }
}