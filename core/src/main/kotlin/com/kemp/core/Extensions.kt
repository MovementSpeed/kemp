package com.kemp.core

import com.artemis.Component
import com.artemis.ComponentMapper
import com.kemp.core.utils.Float2
import com.kemp.core.utils.length

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