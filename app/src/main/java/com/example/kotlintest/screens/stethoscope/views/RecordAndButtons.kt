package com.example.kotlintest.screens.stethoscope.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlintest.screens.stethoscope.StethoScopeState
import com.example.kotlintest.screens.stethoscope.models.DeleteBtnStatus
import com.example.kotlintest.screens.stethoscope.models.PlayBtnStatus

@Composable
fun RecordAndButtons(
    uiState1: StethoScopeState,
    onRecordClick: () -> Unit,
    onCheckClick: () -> Unit,
    onPLayClick: (String?, playIcon: PlayBtnStatus) -> Unit,
    onDeleteClicked: (String?, DeleteBtnStatus) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(40.dp)) {
        RecordSections(
            uiState = uiState1, onPlayClicked = { path, status -> onPLayClick(path, status) },
            onDeleteClicked = { path, phase -> onDeleteClicked(path, phase) })
        IconSections(
            uiState1.recordState,
            uiState1.checkIcon,
            onRecordClick = onRecordClick,
            onCheckClick = onCheckClick,
        )
    }
}




