package com.kalary.larkisedosti.ui

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper

fun playBlip() {
    try {
        val tone = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 30)
        tone.startTone(ToneGenerator.TONE_PROP_ACK, 70)
        Handler(Looper.getMainLooper()).postDelayed({ tone.release() }, 180)
    } catch (_: RuntimeException) {
    }
}
