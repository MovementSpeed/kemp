package com.kemp.core.ecs.systems

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.kemp.core.ecs.components.RigidBodyComponent
import com.kemp.core.ecs.components.TransformComponent
import org.ode4j.ode.*

class PhysicsSystem : BaseSystem(), DGeom.DNearCallback {
    val physicsWorld: DWorld
    val space: DHashSpace

    private val jointGroup: DJointGroup

    init {
        OdeConfig.setLibCCDEndabled(true)
        OdeHelper.initODE2(0)

        physicsWorld = OdeHelper.createWorld()
        space = OdeHelper.createHashSpace(null)
        jointGroup = OdeHelper.createJointGroup()

        setupWorld()
    }

    override fun processSystem() {
        space.collide(0, this)
        physicsWorld.quickStep(0.05)
        jointGroup.empty()
    }

    override fun call(data: Any?, o1: DGeom?, o2: DGeom?) {
        val b1 = o1?.body ?: return
        val b2 = o2?.body ?: return

        val contactBuffer = DContactBuffer(20)
        val numberOfCollisions = OdeHelper.collide(
            o1, o2, 20, contactBuffer.geomBuffer
        )

        if (numberOfCollisions > 0) {
            repeat(numberOfCollisions) { index ->
                val contact = contactBuffer[index]
                val surface = contact.surface

                surface.mode = OdeConstants.dContactBounce or OdeConstants.dContactSoftCFM
                surface.mu = OdeConstants.dInfinity
                surface.mu2 = 0.0
                surface.bounce = 0.01
                surface.bounce_vel = 0.1
                surface.soft_cfm = 0.01

                val joint = OdeHelper.createContactJoint(physicsWorld, jointGroup, contact)
                joint.attach(b1, b2)
            }
        }
    }

    private fun setupWorld() {
        physicsWorld.setGravity(0.0, -0.1, 0.0)
        physicsWorld.erp = 0.2
        physicsWorld.cfm = 1e-5
        physicsWorld.autoDisableFlag = true
        physicsWorld.linearDamping = 0.00001
        physicsWorld.angularDamping = 0.005
        physicsWorld.maxAngularSpeed = 200.0
        physicsWorld.contactMaxCorrectingVel = 0.9
        physicsWorld.contactSurfaceLayer = 0.001
    }
}