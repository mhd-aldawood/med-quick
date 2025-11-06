package com.example.kotlintest.core.audio

import android.content.Context
import javax.inject.Inject

class AudioProcessor @Inject constructor(private val context: Context) {

    private var writer: PcmFileWriter? = null
    private var pcmPath: String = ""

    // set these correctly for your source
    private val sampleRate = 44100
    private val channels = 1 // 2 if onProcessData provides interleaved stereo

    fun startCapture() {
        writer = PcmFileWriter(context, sampleRate, channels).also {
            pcmPath = it.start() // keep path for later playback
        }
    }

    fun stopCapture() {
        writer?.close()
        writer = null
    }

    fun onProcessData(p0: ShortArray?) {
        p0 ?: return
        writer?.writeChunk(p0)
    }


    fun recordedFilePath(): String = pcmPath
}
