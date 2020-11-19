package com.kemp.core.physics

import com.kemp.core.Entity
import com.kemp.core.ecs.components.RigidBodyComponent
import com.kemp.core.mapper
import org.ode4j.ode.DSpace
import org.ode4j.ode.DWorld
import org.ode4j.ode.OdeHelper

class Physics(private val world: DWorld, private val space: DSpace) {
    fun createRigidBody(entity: Entity, type: RigidBodyComponent.Type) {
        mapper<RigidBodyComponent>()
            .create(entity)
            .createAs(type, world, space)
    }
}