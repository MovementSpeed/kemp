package com.kemp.core.audio

interface Sound {
    fun play()
    fun loop()
    fun stop()
    fun pause()
    fun resume()
    fun volume(v: Float)
}