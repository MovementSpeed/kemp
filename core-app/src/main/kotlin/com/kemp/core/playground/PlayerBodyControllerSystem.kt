package com.kemp.core.playground

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.Position
import com.kemp.core.Rotation
import com.kemp.core.ecs.components.*
import com.kemp.core.input.touch.TouchStick
import com.kemp.core.utils.*
import kotlin.math.*

@All(TransformComponent::class, NodeComponent::class, TouchElementsComponent::class, PlayerComponent::class, ModelAnimatorComponent::class)
class PlayerBodyControllerSystem : IteratingSystem() {
    private lateinit var rigidBodyMapper: ComponentMapper<RigidBodyComponent>
    private lateinit var touchElementsMapper: ComponentMapper<TouchElementsComponent>
    private lateinit var modelAnimatorMapper: ComponentMapper<ModelAnimatorComponent>

    private var movement = Float3()
    private var currentAngle = 0f

    override fun process(entityId: Int) {

    }

    private fun angleDelta(x: Float, y: Float): Float {
        var arg: Float = (y - x) % 360f
        if (arg < 0f) arg += 360f
        if (arg > 180f) arg -= 360f
        return -arg
    }
}