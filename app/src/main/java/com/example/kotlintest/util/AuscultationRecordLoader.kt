package com.example.kotlintest.util

import android.content.Context
import com.example.kotlintest.screens.stethoscope.models.AuscultationRecord
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject
//TODO move this class from here
class AuscultationRecordLoader @Inject constructor(
    private val context: Context
) {

    // Function to load PCM files and bind to AuscultationRecord
    fun loadAuscultationRecords(): MutableList<AuscultationRecord> {
        val auscultationRecords = mutableListOf<AuscultationRecord>()

        // Access private files directory
        val privateFilesDir = context.getExternalFilesDir(null)
        val pcmFiles =
            privateFilesDir?.listFiles { file -> file.name.endsWith(".pcm") } // Filter .pcm files

        pcmFiles?.forEach { pcmFile ->
            try {
                val heartWaveData = readPcmDataFromFile(pcmFile)
                val auscultationRecord = AuscultationRecord(
                    title = pcmFile.nameWithoutExtension,
                    duration = calculateDuration(heartWaveData), // Example, you can calculate duration based on data
                    heartWave = heartWaveData,
                    isCashed = true,
                    file = pcmFile.absolutePath
                )
                auscultationRecords.add(auscultationRecord)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return auscultationRecords
    }

    // Function to read PCM data (ShortArray) from the PCM file
    private fun readPcmDataFromFile(file: File): List<Short> {
        val heartWave = mutableListOf<Short>()
        val buffer = ByteArray(4096)

        try {
            val fileInputStream = FileInputStream(file)
            var bytesRead: Int
            while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                // Convert byte array to short array (Little Endian)
                val shortArray = ByteBuffer.wrap(buffer, 0, bytesRead)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .asShortBuffer()
                    .let { shortBuffer ->
                        val shorts = ShortArray(shortBuffer.remaining())
                        shortBuffer.get(shorts)
                        shorts
                    }

                heartWave.addAll(shortArray.toList())
            }
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return heartWave
    }

    // Calculate duration based on the sample rate and number of samples
    // You might want to customize this logic based on your actual PCM format
    private fun calculateDuration(heartWave: List<Short>): String {
        val sampleRate = 44100 // Example sample rate, adjust to your actual sample rate
        val durationInSeconds = heartWave.size / sampleRate.toFloat()
        val minutes = (durationInSeconds / 60).toInt()
        val seconds = (durationInSeconds % 60).toInt()
        return String.format("%02d:%02d", minutes, seconds)
    }
}
