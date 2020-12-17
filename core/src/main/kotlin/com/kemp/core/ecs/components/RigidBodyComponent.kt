package com.kemp.core.ecs.components

import com.artemis.Component
import com.kemp.core.Position
import com.kemp.core.Rotation
import com.kemp.core.toQuaternion
import com.kemp.core.toVector3
import com.kemp.core.utils.Float3
import org.ode4j.ode.*

class RigidBodyComponent : Component() {
    var geom: DGeom? = null
    var body: DBody? = null

    lateinit var type: Type

    private var initialized = false
    private lateinit var world: DWorld
    private lateinit var space: DSpace
    private var mass: DMass? = null

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
        }
    }

    private fun box(box: Type.Box) {
        body = OdeHelper.createBody(world)
        setup(box, body)

        mass?.apply {
            setBox(
                box.density,
                box.lengthX,
                box.lengthY,
                box.lengthZ
            )

            body?.mass = this
        }

        geom = OdeHelper.createBox(
            space,
            box.lengthX,
            box.lengthY,
            box.lengthZ
        )

        geom?.body = body
    }

    private fun setup(type: Type, body: DBody?) {
        body?.position = Position().toVector3()
        body?.quaternion = Rotation().toQuaternion()

        if (type.kinematic) {
            body?.setKinematic()
        } else {
            body?.autoDisableFlag = false
            body?.enable()
            mass = OdeHelper.createMass()
        }
    }

    sealed class Type(open val kinematic: Boolean) {
        class Box(val lengthX: Double, val lengthY: Double, val lengthZ: Double, val density: Double = 0.0, kinematic: Boolean = false) : Type(kinematic)
    }
}