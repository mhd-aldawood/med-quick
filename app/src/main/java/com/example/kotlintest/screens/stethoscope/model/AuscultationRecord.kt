package com.example.kotlintest.screens.stethoscope.model

import com.example.kotlintest.R
import java.util.UUID


data class AuscultationRecord(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val duration: String,
    val heartWave: List<Short>,
    val isCashed: Boolean,
    val recordingIcon: Int = R.mipmap.ic_recording,
    val playIcon: PlayBtnStatus = PlayBtnStatus.Play,
    val deleteBtn: DeleteBtnStatus = DeleteBtnStatus.DeleteIcon,
    val file: String? = null
)

enum class DeleteBtnStatus(val value: Int) {
    DeleteIcon(R.mipmap.ic_delete),
    SureIcon(R.mipmap.ic_sure)
}

enum class PlayBtnStatus(val value: Int) {
    Play(R.drawable.ic_play),
    Pause(R.mipmap.ic_pause_recording)
}