package com.kemp.android

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.media.SoundPool
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun SoundPool.suspendLoad(fd: AssetFileDescriptor): Int =
    suspendCoroutine {
        val soundId = load(fd, 1)
        setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0 && sampleId == soundId) {
                it.resume(soundId)
            }
        }
    }

fun MediaPlayer.play() {
    isLooping = false
    seekTo(0)
    setOnSeekCompleteListener {
        start()
    }
}

fun MediaPlayer.loop() {
    isLooping = true
    seekTo(0)
    setOnSeekCompleteListener {
        start()
    }
}