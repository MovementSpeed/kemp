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
        get() = transpose(matrix).toFloatArray()

    private var matrix = com.kemp.core.utils.scale(scale) *
            com.kemp.core.utils.rotation(rotation) *
            translation(position)

    fun update() {
        position.xyz = matrix.position
        rotation.xyz = matrix.rotation
        scale.xyz = matrix.scale
        forward.xyz = matrix.forward
        up.xyz = matrix.up
    }

    fun translate(v: Float3): Transform {
        matrix *= translation(v)
        return this
    }

    /**
     * @param v: v.x => pitch, v.y => yaw, v.z => roll
     */
    fun rotate(v: Float3): Transform {
        matrix *= com.kemp.core.utils.rotation(v)
        return this
    }

    fun scale(v: Float3): Transform {
        matrix *= com.kemp.core.utils.scale(v)
        return this
    }

    fun position(v: Position): Transform {
        return translate(-matrix.position)
            .translate(v)
    }

    fun rotation(v: Rotation): Transform {
        return rotate(-matrix.rotation)
            .rotate(v)
    }

    fun scaling(v: Scale): Transform {
        return scale(-matrix.scale)
            .scale(v)
    }
}