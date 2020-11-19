package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.utils.radians
import org.ode4j.math.DMatrix3
import org.ode4j.math.DQuaternion
import org.ode4j.math.DVector3
import org.ode4j.ode.*
import kotlin.random.Random

class RigidBodyComponent : Component() {
    var geom: DGeom? = null
    lateinit var type: Type

    private lateinit var world: DWorld
    private lateinit var space: DSpace

    var body: DBody? = null
    private lateinit var mass: DMass

    fun createAs(type: Type, world: DWorld, space: DSpace) {
        this.type = type
        this.world = world
        this.space = space

        when (type) {
            is Type.Box -> box(type)
            is Type.Plane -> plane(type)
        }
    }

    private fun box(box: Type.Box) {
        body = OdeHelper.createBody(world)
        body?.position = DVector3(0.0, 10.0, 0.0)
        body?.quaternion = DQuaternion(
            radians(Random.nextFloat() * 360f).toDouble(),
            Random.nextDouble(),
            Random.nextDouble(),
            Random.nextDouble())

        mass = OdeHelper.createMass()
        mass.setBox(
            box.density,
            box.lengthX,
            box.lengthY,
            box.lengthZ
        )

        body?.mass = mass

        geom = OdeHelper.createBox(
            space,
            box.lengthX,
            box.lengthY,
            box.lengthZ
        )

        geom?.body = body
    }

    private fun plane(plane: Type.Plane) {
        body = OdeHelper.createBody(world)
        body?.position = DVector3(0.0, 0.0, 0.0)
        body?.setKinematic()

        geom = OdeHelper.createBox(
            space,
            plane.lengthX,
            plane.lengthY,
            plane.lengthZ
        )

        geom?.body = body
    }

    sealed class Type {
        data class Box(val density: Double, val lengthX: Double, val lengthY: Double, val lengthZ: Double) : Type()
        data class Plane(val lengthX: Double, val lengthY: Double, val lengthZ: Double) : Type()
    }
}