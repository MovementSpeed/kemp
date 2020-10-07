package com.kemp.android.input

import com.kemp.core.input.methods.KeyboardInput

class AndroidKeyboardInput : KeyboardInput {
    override fun update() {
    }

    override fun key(code: Int): Boolean {
        return false
    }
}