package com.example.kotlintest.core.audio

import android.content.Context
import com.example.kotlintest.util.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AudioProcessor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pcmPlayer: PcmPlayer,
    private val pcmFileReader: PcmFileReader
) {

    private var writer: PcmFileWriter? = null
    private var pcmPath: String = ""

    // set these correctly for your source
    private val sampleRate = 44100
    private val channels = 1 // 2 if onProcessData provides interleaved stereo

    init {
        pcmPlayer.init()
    }

    fun startCapture() {
        writer = PcmFileWriter(context, sampleRate, channels).also {
            pcmPath = it.start() // keep path for later playback
        }
        pcmPlayer.play()
    }

    fun stopCapture(release: Boolean = false) {
        writer?.close()
        writer = null
        pcmPlayer.stop()

        if (release)
            pcmPlayer.release()// to be able to hear the voice again
    }

    private val TAG = "AudioProcessor"
    fun onProcessData(p0: ShortArray?) {
        p0 ?: return
        Logger.i(TAG, "onProcessData" + p0.size.toString())
        writer?.writeChunk(p0)
        pcmPlayer.writeData(p0)
    }

    fun recordedFilePath(): String = pcmPath

    fun playRecord(recordPath: String, onEnd: () -> Unit) {
        Logger.i(TAG, "playRecord${pcmPlayer.isInit()}")
        if (pcmPlayer.isInit()) {
            pcmPlayer.play()
            Logger.i(TAG, "playRecord start")
            pcmFileReader.streamPcmFile(File(recordPath), onEnd = { onEnd() }) {
                Logger.i(TAG, "streamPcmFile callback")
                pcmPlayer.writeData(it)
            }
        }
    }

    fun stopPlayingRecord() {
        pcmPlayer.stop()
    }
}
