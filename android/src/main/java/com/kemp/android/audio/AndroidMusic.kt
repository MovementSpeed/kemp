package com.kemp.android.audio

import android.media.MediaPlayer
import com.kemp.android.loop
import com.kemp.android.play
import com.kemp.core.audio.Music

class AndroidMusic(private val mediaPlayer: MediaPlayer) : Music {
    override fun play() {
        mediaPlayer.play()
    }

    override fun loop() {
        mediaPlayer.loop()
    }

    override fun stop() {
        mediaPlayer.stop()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun resume() {
        mediaPlayer.start()
    }

    override fun volume(v: Float) {
        mediaPlayer.setVolume(v, v)
    }

    override fun rate(r: Float) {
        val params = mediaPlayer.playbackParams
        params.speed = r

        mediaPlayer.playbackParams = params
    }

    override fun playing(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun dispose() {
        mediaPlayer.release()
    }
}