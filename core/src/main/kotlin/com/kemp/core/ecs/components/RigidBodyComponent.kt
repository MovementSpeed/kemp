package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.Position
import com.kemp.core.Rotation
import com.kemp.core.toQuaternion
import com.kemp.core.toVector3
import com.kemp.core.utils.Float3
import com.kemp.core.utils.radians
import org.ode4j.math.DQuaternion
import org.ode4j.math.DVector3
import org.ode4j.ode.*
import kotlin.random.Random

class RigidBodyComponent : Component() {
    var geom: DGeom? = null
    var body: DBody? = null

    lateinit var type: Type

    private var initialized = false
    private lateinit var world: DWorld
    private lateinit var space: DSpace
    private lateinit var mass: DMass

    fun initialized(): Boolean {
        return initialized
    }

    fun initWith(position: Position, rotation: Rotation = Float3()) {
        body?.position = position.toVector3()
        body?.quaternion = rotation.toQuaternion()
        initialized = true
    }

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
        body?.position = Position().toVector3()
        body?.quaternion = Rotation().toQuaternion()

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
        body?.position = Position().toVector3()
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