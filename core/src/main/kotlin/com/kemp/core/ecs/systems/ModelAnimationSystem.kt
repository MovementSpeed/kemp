package com.kemp.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.ecs.components.ModelAnimatorComponent

@All(ModelAnimatorComponent::class)
class ModelAnimationSystem : IteratingSystem() {
    private lateinit var modelAnimatorMapper: ComponentMapper<ModelAnimatorComponent>

    override fun process(entityId: Int) {
        val modelAnimatorComponent = modelAnimatorMapper.get(entityId) ?: return
        val modelAnimator = modelAnimatorComponent.modelAnimator
        modelAnimator ?: return

        if (modelAnimator.animationsCount() == 0) return

        val duration = modelAnimator.animationDuration(0)

        val lastFrameTimestamp = modelAnimatorComponent.lastFrameTimestamp
        val nowTimestamp = System.currentTimeMillis()
        val diffSeconds = (nowTimestamp - lastFrameTimestamp).toFloat() / 1000f

        modelAnimatorComponent.currentTime += diffSeconds * modelAnimatorComponent.playbackSpeed

        if (modelAnimatorComponent.currentTime > duration) {
            modelAnimatorComponent.currentTime = 0f
        } else if (modelAnimatorComponent.currentTime < 0f) {
            modelAnimatorComponent.currentTime = duration
        }

        modelAnimator.animate(0, modelAnimatorComponent.currentTime)
        modelAnimator.update()

        modelAnimatorComponent.lastFrameTimestamp = nowTimestamp
    }
}