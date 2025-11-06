package com.example.kotlintest.core.audio

import android.content.Context
import com.example.kotlintest.util.Logger
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


class PcmFileWriter(
    private val context: Context,
    private val sampleRate: Int,
    private val channels: Int // 1 = mono, 2 = stereo (interleaved)
) {
    private lateinit var file: File
    private var out: BufferedOutputStream? = null
    private val scratch = ByteArray(8192 * 2) // 8k samples -> 16k bytes

    fun start(fileName: String = "cap_${System.currentTimeMillis()}.pcm"): String {
        // App-specific external dir; visible via ADB, no permissions needed
        val dir = context.getExternalFilesDir(null)
//        val dir = getExternalFilesDir(null)
        Logger.d("PathTest", "App external files dir: ${dir?.absolutePath}")
        dir?.mkdirs()
        java.io.File(dir, "test_from_app.txt").writeText("hello from app")

        file = File(context.getExternalFilesDir(null), fileName)
        out = BufferedOutputStream(FileOutputStream(file))
        return file.absolutePath
    }

    fun writeChunk(samples: ShortArray) {
        val o = out ?: return
        var offset = 0
        // write in slices that fit our scratch buffer
        while (offset < samples.size) {
            val count = minOf((scratch.size / 2), samples.size - offset)
            // pack little-endian 16-bit
            var i = 0
            var j = offset
            while (i < count) {
                val v = samples[j].toInt()
                scratch[i * 2] = (v and 0xFF).toByte()
                scratch[i * 2 + 1] = ((v ushr 8) and 0xFF).toByte()
                i++
                j++
            }
            o.write(scratch, 0, count * 2)
            offset += count
        }
    }

    fun close() {
        out?.flush()
        out?.close()
        out = null
    }

    fun path(): String = file.absolutePath
}

