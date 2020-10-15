package com.kemp.core.audio

import com.kemp.core.interfaces.Disposable

interface Sound : Disposable {
    fun play()
    fun loop()
    fun stop()
    fun pause()
    fun resume()
    fun volume(v: Float)
    fun rate(r: Float)
    fun playing(): Boolean
}