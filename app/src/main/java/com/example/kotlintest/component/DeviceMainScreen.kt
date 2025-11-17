package com.example.kotlintest.component

import androidx.compose.runtime.Composable

@Composable
fun DeviceMainScreen(
    titleIcon: Int,
    title: String,
    cancelText: String ,
    cancelIcon: Int ,
    onCancelClick: () -> Unit,
    content: @Composable () -> Unit
) {
    MainScaffold() { innerPadding ->
        DeviceConnectionHeader(
            titleIcon = titleIcon,
            title = title,
            cancelText = cancelText,
            cancelIcon = cancelIcon,
            onCancelClick = onCancelClick,
            innerPadding = innerPadding,
        ) {
            content()
        }
    }
}