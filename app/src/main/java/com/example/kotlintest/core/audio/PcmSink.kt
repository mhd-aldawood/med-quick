package com.example.kotlintest.core.audio

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PcmSink(
    private val context: Context,
    private val sampleRate: Int,
    private val channels: Int
) {
    private lateinit var pcmFile: File
    private var fos: FileOutputStream? = null
    val path get() = pcmFile.absolutePath

    fun start() {
        pcmFile = File(context.cacheDir, "capture_${System.currentTimeMillis()}.pcm")
        fos = FileOutputStream(pcmFile)
    }

    fun writeChunk(samples: ShortArray) {
        val out = fos ?: return
        val bb = ByteBuffer.allocate(samples.size * 2).order(ByteOrder.LITTLE_ENDIAN)
        bb.asShortBuffer().put(samples)
        out.write(bb.array())
    }

    fun close() {
        fos?.flush()
        fos?.close()
        fos = null
    }
}
