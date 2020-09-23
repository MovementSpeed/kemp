package com.kemp.core.scene

import com.kemp.core.Position
import com.kemp.core.Rotation
import com.kemp.core.Scale
import com.kemp.core.utils.*
import kotlin.concurrent.timer
import kotlin.math.cos
import kotlin.math.sin

class Transform {
    val position: Position = Position(0f, 0f, 0f)
    val rotation: Rotation = Rotation(0f, 0f, 0f)
    val scale: Scale = Scale(1f, 1f, 1f)
    val forward: Float3 = Float3(0f, 0f, 1f)
    val up: Float3 = Float3(0f, 1f, 0f)
    val array: FloatArray
        get() = matrix.toFloatArray()

    private var matrix = transpose(com.kemp.core.utils.scale(scale) *
            com.kemp.core.utils.rotation(rotation) *
            translation(position))

    fun transpose(): Transform {
        transposeFast(matrix)
        return this
    }

    fun translate(v: Float3): Transform {
        transposeFast(matrix)

        matrix[0][3] += matrix[0][0] * v.x + matrix[0][1] * v.y + matrix[0][2] * v.z
        matrix[1][3] += matrix[1][0] * v.x + matrix[1][1] * v.y + matrix[1][2] * v.z
        matrix[2][3] += matrix[2][0] * v.x + matrix[2][1] * v.y + matrix[2][2] * v.z
        matrix[3][3] += matrix[3][0] * v.x + matrix[3][1] * v.y + matrix[3][2] * v.z

        transposeFast(matrix)

        position.xyz = matrix.position
        forward.xyz = matrix.forward
        up.xyz = matrix.up

        return this
    }

    /**
     * @param v: v.x => pitch, v.y => yaw, v.z => roll
     */
    fun rotate(v: Float3): Transform {
        transposeFast(matrix)

        val roll = radians(v.z)
        val pitch = radians(v.x)
        val yaw = radians(v.y)

        val hr: Float = roll * 0.5f
        val shr = sin(hr)
        val chr = cos(hr)
        val hp: Float = pitch * 0.5f
        val shp = sin(hp)
        val chp = cos(hp)
        val hy: Float = yaw * 0.5f
        val shy = sin(hy)
        val chy = cos(hy)
        val chyShp = chy * shp
        val shyChp = shy * chp
        val chyChp = chy * chp
        val shyShp = shy * shp

        val x = chyShp * chr + shyChp * shr
        val y = shyChp * chr - chyShp * shr
        val z = chyChp * shr - shyShp * chr
        val w: Float = chyChp * chr + shyShp * shr

        val xx = x * x
        val xy = x * y
        val xz = x * z
        val xw = x * w
        val yy = y * y
        val yz = y * z
        val yw = y * w
        val zz = z * z
        val zw = z * w

        // Set matrix from quaternion
        val r00 = 1 - 2 * (yy + zz)
        val r01 = 2 * (xy - zw)
        val r02 = 2 * (xz + yw)
        val r10 = 2 * (xy + zw)
        val r11 = 1 - 2 * (xx + zz)
        val r12 = 2 * (yz - xw)
        val r20 = 2 * (xz - yw)
        val r21 = 2 * (yz + xw)
        val r22 = 1 - 2 * (xx + yy)

        val m00: Float = matrix[0][0] * r00 + matrix[0][1] * r10 + matrix[0][2] * r20
        val m01: Float = matrix[0][0] * r01 + matrix[0][1] * r11 + matrix[0][2] * r21
        val m02: Float = matrix[0][0] * r02 + matrix[0][1] * r12 + matrix[0][2] * r22
        val m10: Float = matrix[1][0] * r00 + matrix[1][1] * r10 + matrix[1][2] * r20
        val m11: Float = matrix[1][0] * r01 + matrix[1][1] * r11 + matrix[1][2] * r21
        val m12: Float = matrix[1][0] * r02 + matrix[1][1] * r12 + matrix[1][2] * r22
        val m20: Float = matrix[2][0] * r00 + matrix[2][1] * r10 + matrix[2][2] * r20
        val m21: Float = matrix[2][0] * r01 + matrix[2][1] * r11 + matrix[2][2] * r21
        val m22: Float = matrix[2][0] * r02 + matrix[2][1] * r12 + matrix[2][2] * r22
        val m30: Float = matrix[3][0] * r00 + matrix[3][1] * r10 + matrix[3][2] * r20
        val m31: Float = matrix[3][0] * r01 + matrix[3][1] * r11 + matrix[3][2] * r21
        val m32: Float = matrix[3][0] * r02 + matrix[3][1] * r12 + matrix[3][2] * r22

        matrix[0][0] = m00
        matrix[1][0] = m10
        matrix[2][0] = m20
        matrix[3][0] = m30
        matrix[0][1] = m01
        matrix[1][1] = m11
        matrix[2][1] = m21
        matrix[3][1] = m31
        matrix[0][2] = m02
        matrix[1][2] = m12
        matrix[2][2] = m22
        matrix[3][2] = m32

        transposeFast(matrix)

        rotation.xyz = matrix.rotation
        forward.xyz = matrix.forward
        up.xyz = matrix.up

        return this
    }

    fun scale(v: Float3): Transform {
        matrix[0][0] *= v.x
        matrix[0][1] *= v.y
        matrix[0][2] *= v.z

        matrix[1][0] *= v.x
        matrix[1][1] *= v.y
        matrix[1][2] *= v.z

        matrix[2][0] *= v.x
        matrix[2][1] *= v.y
        matrix[2][2] *= v.z

        matrix[3][0] *= v.x
        matrix[3][1] *= v.y
        matrix[3][2] *= v.z

        scale.xyz = matrix.scale
        forward.xyz = matrix.forward
        up.xyz = matrix.up

        return this
    }

    fun position(v: Position) {
        translate(-matrix.position)
            .translate(v)
    }

    fun rotation(v: Rotation) {
        rotate(-matrix.rotation)
            .rotate(v)
    }

    fun scaling(v: Scale) {
        scale(-matrix.scale)
            .scale(v)
    }
}