package com.kemp.android.audio

import android.media.SoundPool
import com.kemp.core.audio.Sound

class AndroidSound(private val soundPool: SoundPool, private val soundId: Int) : Sound {
    private var volume: Float = 1f

    override fun play() {
        val streamId = soundPool.play(soundId, volume, volume, 1, 0, 1f)
    }

    override fun loop() {
        soundPool.play(soundId, volume, volume, 1, -1, 1f)
    }

    override fun stop() {
        soundPool.stop(soundId)
    }

    override fun pause() {
        soundPool.pause(soundId)
    }

    override fun resume() {
        soundPool.resume(soundId)
    }

    override fun volume(v: Float) {
        this.volume = v
        soundPool.setVolume(soundId, volume, volume)
    }
}