package com.kemp.core.input.methods

interface KeyboardInput {
    fun update()

    /**
     * Polls if a key in [Keys] is pressed
     * @param code keycode found in [Keys]
     * @return true if the specified key is pressed, or false if not.
     */
    fun key(code: Int): Boolean
}