package com.kemp.core

import com.artemis.Component

inline fun <reified T : Component> Entity.component(): T {
    val mapper = Kemp.world.getMapper(T::class.java)
    return mapper.get(this)
}