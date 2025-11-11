package com.example.kotlintest.core.audio

import com.example.kotlintest.util.Logger
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.inject.Inject

class PcmFileReader @Inject constructor() {

    companion object {
        private const val TAG = "PcmFileReader"
    }

    /**
     * Stream the PCM file in chunks of short data.
     * @param file The PCM file to read from.
     * @param onChunk Callback to process each chunk of PCM data (ShortArray).
     */
    fun streamPcmFile(file: File, onEnd: () -> Unit, onChunk: (ShortArray) -> Unit) {
        Logger.i(TAG, "streamPcmFile start")

        val buffer = ByteArray(4096)  // Buffer to read bytes from the file
        var bytesRead: Int

        try {
            FileInputStream(file).use { input ->
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    val shorts = convertBytesToShortArray(buffer, bytesRead)
                    onChunk(shorts)  // Pass the chunk to the callback
                }
                onEnd()
            }
        } catch (e: IOException) {
            Logger.e(TAG, "Error reading PCM file", e)
        }
    }

    /**
     * Convert byte buffer into a ShortArray (16-bit little-endian PCM data).
     * @param buffer The byte array to convert.
     * @param bytesRead The number of bytes read from the file.
     * @return A ShortArray containing the PCM data.
     */
    private fun convertBytesToShortArray(buffer: ByteArray, bytesRead: Int): ShortArray {
        val shortArray = ShortArray(bytesRead / 2)  // Each sample is 2 bytes (16-bit)
        var i = 0
        var j = 0
        while (j < bytesRead - 1) {
            val lo = buffer[j].toInt() and 0xFF
            val hi = buffer[j + 1].toInt() and 0xFF
            shortArray[i] = ((hi shl 8) or lo).toShort()
            i++
            j += 2
        }
        return shortArray
    }
}
