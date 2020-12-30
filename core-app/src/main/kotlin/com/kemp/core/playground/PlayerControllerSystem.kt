package com.kemp.core.playground

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.Position
import com.kemp.core.Rotation
import com.kemp.core.ecs.components.ModelAnimatorComponent
import com.kemp.core.ecs.components.NodeComponent
import com.kemp.core.ecs.components.TouchElementsComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.input.touch.TouchStick
import com.kemp.core.utils.*
import kotlin.math.*

@All(TransformComponent::class, NodeComponent::class, TouchElementsComponent::class, PlayerComponent::class, ModelAnimatorComponent::class)
class PlayerControllerSystem : IteratingSystem() {
    private lateinit var transformMapper: ComponentMapper<TransformComponent>
    private lateinit var touchElementsMapper: ComponentMapper<TouchElementsComponent>
    private lateinit var modelAnimatorMapper: ComponentMapper<ModelAnimatorComponent>

    private var movement = Float3()
    private var currentAngle = 0f

    override fun process(entityId: Int) {
        val transformComponent = transformMapper.get(entityId)
        val transform = transformComponent.transform

        val touchSticksComponent = touchElementsMapper.get(entityId) ?: return
        val touchStick = touchSticksComponent.touchElements["rotation"] as? TouchStick? ?: return

        val modelAnimatorComponent = modelAnimatorMapper.get(entityId) ?: return

        this.movement.x = touchStick.stickVector.x / 51f
        this.movement.y = 0f
        this.movement.z = touchStick.stickVector.y / 51f

        val movementSpeed = length(movement)
        println(movementSpeed)

        if (movementSpeed > 0.006f) {
            modelAnimatorComponent.playbackSpeed = movementSpeed * 51f
            if (modelAnimatorComponent.currentTake != playerCrouchWalkTake) {
                modelAnimatorComponent.currentTake = playerCrouchWalkTake
            }

            val dir = degrees(atan2(movement.z, movement.x)) + 90f

            val angleDelta = angleDelta(dir, currentAngle)

            val angularSpeed = 0.1f * angleDelta
            currentAngle += angularSpeed

            transform.rotate(Rotation(0f, -angularSpeed, 0f))
                .translate(Position(0f, 0f, -movementSpeed))
        } else {
            modelAnimatorComponent.playbackSpeed = 1f

            if (modelAnimatorComponent.currentTake != playerCrouchIdleTake) {
                modelAnimatorComponent.currentTake = playerCrouchIdleTake
            }
        }
    }

    private fun angleDelta(x: Float, y: Float): Float {
        var arg: Float = (y - x) % 360f
        if (arg < 0f) arg += 360f
        if (arg > 180f) arg -= 360f
        return -arg
    }
}