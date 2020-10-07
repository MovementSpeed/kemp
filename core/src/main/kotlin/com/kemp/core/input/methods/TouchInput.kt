package com.kemp.core.input.methods

import com.kemp.core.utils.Float2

interface TouchInput {
    /**
     * Updates the state of touch pointers over time.
     */
    fun update()

    /**
     * Polls the touch pointer with the specified index and returns its X position on the screen.
     * @param index the index of the touch pointer. 0 (Zero) means the first finger that touched the screen
     *              and so on.
     * @return the X position of the specified index (or finger) on the screen.
     */
    fun pointerX(index: Int): Float

    /**
     * Polls the touch pointer with the specified index and returns its Y position on the screen.
     * @param index the index of the touch pointer. 0 (Zero) means the first finger that touched the screen
     *              and so on.
     * @return the Y position of the specified index (or finger) on the screen.
     */
    fun pointerY(index: Int): Float

    /**
     * Polls the touch pointer with the specified index and returns its (X, Y) position on the screen.
     * @param index the index of the touch pointer. 0 (Zero) means the first finger that touched the screen
     *              and so on.
     * @return the (X, Y) position of the specified index (or finger) on the screen.
     */
    fun pointer(index: Int): Float2

    /**
     * Polls the touch pointer with the specified index and returns its change over the X axis (delta)
     * relative to its last X position on the screen.
     * @param index the index of the touch pointer. 0 (Zero) means the first finger that touched the screen
     *              and so on.
     * @return the delta of the position of the pointer over the X axis
     */
    fun pointerDeltaX(index: Int): Float

    /**
     * Polls the touch pointer with the specified index and returns its change over the Y axis (delta)
     * relative to its last Y position on the screen.
     * @param index the index of the touch pointer. 0 (Zero) means the first finger that touched the screen
     *              and so on.
     * @return the delta of the position of the pointer over the Y axis
     */
    fun pointerDeltaY(index: Int): Float

    /**
     * Polls the touch pointer with the specified index and returns its change over the (X, Y) axes (delta)
     * relative to its last position on the screen.
     * @param index the index of the touch pointer. 0 (Zero) means the first finger that touched the screen
     *              and so on.
     * @return the delta of the position of the pointer over the (X, Y) axes
     */
    fun pointerDelta(index: Int): Float2
}