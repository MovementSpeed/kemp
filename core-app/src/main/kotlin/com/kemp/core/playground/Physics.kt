package com.kemp.core.playground

import org.ode4j.math.DVector3
import org.ode4j.ode.*
import org.ode4j.ode.OdeConstants.*

class Physics {
    private lateinit var world: DWorld
    private lateinit var space: DHashSpace
    private lateinit var jointGroup: DJointGroup

    private lateinit var boxGeom: DBox

    private val nearCallback = DGeom.DNearCallback { _, o1, o2 ->
        val b1 = o1.body
        val b2 = o2.body

        val contactBuffer = DContactBuffer(20)
        val numberOfCollisions = OdeHelper.collide(o1, o2, 20, contactBuffer.geomBuffer)

        if (numberOfCollisions > 0) {
            repeat(numberOfCollisions) { index ->
                val contact = contactBuffer[index]
                val surface = contact.surface

                surface.mode = dContactBounce or dContactSoftCFM
                surface.mu = dInfinity
                surface.mu2 = 0.0
                surface.bounce = 0.01
                surface.bounce_vel = 0.1
                surface.soft_cfm = 0.01

                val joint = OdeHelper.createContactJoint(world, jointGroup, contact)
                joint.attach(b1, b2)
            }
        }
    }

    fun init() {
        OdeConfig.setLibCCDEndabled(true)
        OdeHelper.initODE2(0)

        world = OdeHelper.createWorld()
        space = OdeHelper.createHashSpace(null)
        jointGroup = OdeHelper.createJointGroup()

        setupWorld()

        OdeHelper.createPlane(space, 0.0, 1.0, 0.0, 0.0)
        setupBox()
    }

    fun update() {
        if (!this::space.isInitialized) return

        space.collide(0, nearCallback)
        world.quickStep(0.05)
        jointGroup.empty()
        draw()
    }

    fun destroy() {
        jointGroup.destroy()
        space.destroy()
        world.destroy()
    }

    private fun draw() {
        val position = boxGeom.position
        val rotation = boxGeom.rotation
        val sides = boxGeom.lengths

        println(
            """
            position: x = ${position.get0()}, y = ${position.get1()}, z = ${position.get2()}
            """.trimIndent())
    }

    private fun setupWorld() {
        world.setGravity(0.0, -1.0, 0.0)
        world.erp = 0.2
        world.cfm = 1e-5
        world.autoDisableFlag = true
        world.linearDamping = 0.00001
        world.angularDamping = 0.005
        world.maxAngularSpeed = 200.0
        world.contactMaxCorrectingVel = 0.9
        world.contactSurfaceLayer = 0.001
    }

    private fun setupBox() {
        val boxBody = OdeHelper.createBody(world)
        boxBody.position = DVector3(0.0, 10.0, 0.0)

        val boxMass = OdeHelper.createMass()
        boxMass.setBox(0.5, 2.0, 2.0, 2.0)

        boxBody.mass = boxMass

        val boxGeom = OdeHelper.createBox(space, 1.0, 1.0, 1.0)
        boxGeom.body = boxBody

        this.boxGeom = boxGeom
    }
}