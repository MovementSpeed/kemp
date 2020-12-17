package com.kemp.core.ecs.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.ecs.components.RigidBodyComponent
import com.kemp.core.ecs.components.TransformComponent
import com.kemp.core.utils.*
import org.ode4j.math.DQuaternion
import org.ode4j.math.DQuaternionC
import kotlin.math.acos
import kotlin.math.sqrt


@All(RigidBodyComponent::class, TransformComponent::class)
class RigidBodyTransformSystem : IteratingSystem() {
    private lateinit var rigidBodyMapper: ComponentMapper<RigidBodyComponent>
    private lateinit var transformMapper: ComponentMapper<TransformComponent>
    private val float3 = Float3()

    override fun process(entityId: Int) {
        val rigidBodyComponent = rigidBodyMapper.get(entityId) ?: return
        val body = rigidBodyComponent.body ?: return

        val transformComponent = transformMapper.get(entityId) ?: return
        val transform = transformComponent.transform

        when (rigidBodyComponent.initialized()) {
            true -> {
                // Takes the rigid body transform and applies it to the rendering transform
                val bodyPosition = body.position
                val rotation = body.quaternion as DQuaternion

                val (axis, angle) = quaternionToAxisAngle(rotation)
                val rot = rotation(axis, degrees(angle))

                float3.x = bodyPosition.get0().toFloat()
                float3.y = bodyPosition.get1().toFloat()
                float3.z = bodyPosition.get2().toFloat()
                val tra = translation(float3)

                transform.matrix = tra * rot
            }

            false -> {
                // Takes the rendering transform and initializes the rigid body transform
                rigidBodyComponent.initWith(
                    transform.position,
                    transform.rotation
                )
            }
        }
    }

    private fun quaternionToAxisAngle(q1: DQuaternion): Pair<Float3, Float> {
        if (q1.get0() > 1) q1.normalize() // if w>1 acos and sqrt will produce errors, this cant happen if quaternion is normalised

        val angle = 2.0f * acos(q1.get0())

        val x: Float
        val y: Float
        val z: Float

        val s = sqrt(1 - q1.get0() * q1.get0()) // assuming quaternion normalised then w is less than 1, so term always positive.
        if (s < 0.001) { // test to avoid divide by zero, s is always positive due to sqrt
            // if s close to zero then direction of axis not important
            x = q1.get1().toFloat() // if it is important that axis is normalised then replace with x=1; y=z=0;
            y = q1.get2().toFloat()
            z = q1.get3().toFloat()
        } else {
            x = q1.get1().toFloat() / s.toFloat() // normalise axis
            y = q1.get2().toFloat() / s.toFloat()
            z = q1.get3().toFloat() / s.toFloat()
        }

        return Float3(x, y, z) to angle.toFloat()
    }
}