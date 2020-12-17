package com.kemp.core

import com.artemis.Component
import com.artemis.ComponentMapper
import com.kemp.core.utils.*
import org.ode4j.math.DQuaternion
import org.ode4j.math.DQuaternionC
import org.ode4j.math.DVector3
import org.ode4j.math.DVector3C
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

inline fun <reified T : Component> Entity.component(): T {
    val mapper = Kemp.world.getMapper(T::class.java)
    return mapper.get(this)
}

inline fun <reified T : Component> mapper(): ComponentMapper<T> {
    return Kemp.world.getMapper(T::class.java)
}

fun Float2.normalize(): Float2 {
    val l = 1.0f / length(this)
    this.x = this.x * l
    this.y = this.y * l
    return this
}

fun Mat4.toMat3(): Mat3 {
    return Mat3.of(*this.toFloatArray())
}

fun Rotation.toQuaternion(): DQuaternionC {
    val pitch: Float = radians(y)
    val yaw: Float = radians(x)
    val roll: Float = radians(z)

    val qx = sin(roll / 2.0) * cos(pitch / 2.0) * cos(yaw / 2.0) - cos(roll / 2.0) * sin(pitch / 2.0) * sin(yaw / 2.0)
    val qy = cos(roll / 2.0) * sin(pitch / 2.0) * cos(yaw / 2.0) + sin(roll / 2.0) * cos(pitch / 2.0) * sin(yaw / 2.0)
    val qz = cos(roll / 2.0) * cos(pitch / 2.0) * sin(yaw / 2.0) - sin(roll / 2.0) * sin(pitch / 2.0) * cos(yaw / 2.0)
    val qw = cos(roll / 2.0) * cos(pitch / 2.0) * cos(yaw / 2.0) + sin(roll / 2.0) * sin(pitch / 2.0) * sin(yaw / 2.0)

    return DQuaternion(qw, qx, qy, qz)
}

fun Position.toVector3(): DVector3C {
    return DVector3(x.toDouble(), y.toDouble(), z.toDouble())
}

fun DQuaternionC.toRotation(): Rotation {
    val w = get0()
    val x = get1()
    val y = get2()
    val z = get3()

    val t0 = +2.0 * (w * x + y * z)
    val t1 = +1.0 - 2.0 * (x * x + y * y)
    val roll = atan2(t0, t1)
    var t2 = +2.0 * (w * y - z * x)
    t2 = if (t2 > 1.0) 1.0 else t2
    t2 = if (t2 < -1.0) -1.0 else t2
    val pitch = asin(t2)
    val t3 = +2.0 * (w * z + x * y)
    val t4 = +1.0 - 2.0 * (y * y + z * z)
    val yaw = atan2(t3, t4)

    return Rotation(degrees(pitch.toFloat()), degrees(yaw.toFloat()), degrees(roll.toFloat()))
}

fun DVector3C.toPosition(): Position {
    return Position(get0().toFloat(), get1().toFloat(), get2().toFloat())
}