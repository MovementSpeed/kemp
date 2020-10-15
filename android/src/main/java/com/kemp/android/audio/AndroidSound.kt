package com.kemp.android.audio

import android.media.SoundPool
import com.kemp.core.audio.Sound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AndroidSound(private val soundPool: SoundPool, private val soundId: Int, private val duration: Int) : Sound {
    private var playing = false
    private var paused = false

    private var volume: Float = 1f
    private var rate: Float = 1f

    private var activeStream = -1
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun play() {
        coroutineScope.launch {
            activeStream = soundPool.play(soundId, volume, volume, 1, 0, rate)
            playing = true
            paused = false
            delay(duration.toLong())
            playing = false
        }
    }

    override fun loop() {
        activeStream = soundPool.play(soundId, volume, volume, 1, -1, rate)
        playing = true
        paused = false
    }

    override fun stop() {
        soundPool.stop(activeStream)
        playing = false
        paused = false
    }

    override fun pause() {
        if (playing) {
            playing = false
            paused = true
            soundPool.pause(activeStream)
        }
    }

    override fun resume() {
        if (paused) {
            playing = true
            paused = false
            soundPool.resume(activeStream)
        }
    }

    override fun volume(v: Float) {
        volume = v
        soundPool.setVolume(activeStream, v, v)
    }

    override fun rate(r: Float) {
        rate = r
        soundPool.setRate(activeStream, r)
    }

    override fun playing(): Boolean {
        return playing
    }

    override fun dispose() {
        soundPool.unload(soundId)
    }
}