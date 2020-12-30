package com.kemp.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.Kemp
import com.kemp.core.ecs.components.ModelAnimatorComponent

@All(ModelAnimatorComponent::class)
class ModelAnimationSystem : IteratingSystem() {
    private lateinit var modelAnimatorMapper: ComponentMapper<ModelAnimatorComponent>

    override fun process(entityId: Int) {
        val modelAnimatorComponent = modelAnimatorMapper.get(entityId) ?: return
        val modelAnimator = modelAnimatorComponent.modelAnimator
        modelAnimator ?: return

        if (modelAnimator.animationsCount() == 0) return
        val currentTake = modelAnimatorComponent.currentTake ?: return

        val frameStartTime = currentTake.frameStart * modelAnimatorComponent.frameDuration
        val frameEndTime = currentTake.frameEnd * modelAnimatorComponent.frameDuration
        val takeDuration = frameEndTime - frameStartTime

        val lastFrameTimestamp = Kemp.previousFrameTimeNanos
        val nowTimestamp = Kemp.frameTimeNanos
        val diffSeconds = ((nowTimestamp - lastFrameTimestamp).toDouble() / 1000000000.0).toFloat()

        currentTake.currentTime += modelAnimatorComponent.frameDuration * modelAnimatorComponent.playbackSpeed //diffSeconds * modelAnimatorComponent.playbackSpeed
        val halfFrameTolerance = modelAnimatorComponent.frameDuration * 0.5f

        if (currentTake.currentTime + halfFrameTolerance > takeDuration) {
            currentTake.currentTime = 0f
        } else if (currentTake.currentTime < 0f) {
            currentTake.currentTime = takeDuration
        }

        val actualTime = frameStartTime + currentTake.currentTime

        modelAnimator.animate(0, actualTime)
        modelAnimator.update()
    }
}