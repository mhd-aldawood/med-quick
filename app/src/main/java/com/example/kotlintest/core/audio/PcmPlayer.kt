package com.example.kotlintest.core.audio


import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import javax.inject.Inject

class PcmPlayer @Inject constructor() {
    private var pcmDataHandler: Handler

    @Volatile
    private var isInit = false
    private lateinit var audioTrack: AudioTrack

    init {
        val handlerThread = HandlerThread(PCM_PLAYER_THREAD_NAME).also { it.start() }
        pcmDataHandler = object : Handler(handlerThread.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.obj is ShortArray) {
                    val data = msg.obj as ShortArray
                    if (data.isEmpty()) {
                        return
                    }

                    audioTrack.write(data, 0, data.size)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    fun init() {
        Log.d(TAG, "init")
        if (isInit) {
            Log.w(TAG, "init, it's already initialized")
            return
        }

        val streamType = AudioManager.STREAM_MUSIC
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val modeType = AudioTrack.MODE_STREAM

        val minBufferSize =
            AudioTrack.getMinBufferSize(PCM_SAMPLE_RATE_HZ, channelConfig, audioFormat)
        try {
            audioTrack = AudioTrack(
                streamType,
                PCM_SAMPLE_RATE_HZ,
                channelConfig,
                audioFormat,
                minBufferSize * 2,
                modeType,
            )
            isInit = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun play() {
        Log.d(TAG, "play")
        if (!isInit) {
            Log.w(TAG, "play, it's not initialized")
            return
        }

        try {
            audioTrack.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        Log.d(TAG, "stop")
        if (!isInit) {
            Log.w(TAG, "play, it's not initialized")
            return
        }

        pcmDataHandler.removeCallbacksAndMessages(null)
        try {
            audioTrack.pause()
            audioTrack.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            audioTrack.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        Log.d(TAG, "release")
        stop()

        try {
            pcmDataHandler.looper.quitSafely()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            audioTrack.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        isInit = false
    }

    fun writeData(dataList: ShortArray?) {
        if (dataList == null || dataList.isEmpty()) {
            return
        }

        if (!isInit) {
            Log.w(TAG, "play, it's not initialized")
            return
        }

        val realData = dataList.clone()
        val msg = Message.obtain().also {
            it.obj = realData
        }
        pcmDataHandler.sendMessage(msg)
    }

    private companion object {
        private const val TAG = "PcmPlayer"
        private const val PCM_PLAYER_THREAD_NAME = "PcmPlayerThread"
        private const val PCM_SAMPLE_RATE_HZ = 8_000
    }

    public fun isInit(): Boolean {
        return isInit
    }
}
