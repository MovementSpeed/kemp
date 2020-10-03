package com.kemp.android.input

import android.view.KeyEvent
import com.kemp.core.input.KeyboardInput

class AndroidKeyboardInput : KeyboardInput, KeyEvent.Callback {
    private val activeKeys = mutableSetOf<Int>()

    override fun key(code: Int): Boolean {
        return activeKeys.contains(code)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        println("DOWN $keyCode")
        activeKeys.add(keyCode)
        return false
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        println("LONG $keyCode")
        activeKeys.add(keyCode)
        return false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        println("UP $keyCode")
        activeKeys.remove(keyCode)
        return false
    }

    override fun onKeyMultiple(keyCode: Int, count: Int, event: KeyEvent?): Boolean {
        println("MULTIPLE $keyCode")
        activeKeys.add(keyCode)
        return false
    }
}