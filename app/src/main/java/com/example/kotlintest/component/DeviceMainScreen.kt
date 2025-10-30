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
    MainScaffold() {
        DeviceConnectionHeader(//TODO add some padding to bottom of the header
            titleIcon = titleIcon,
            title = title,
            cancelText = cancelText,
            cancelIcon = cancelIcon,
            onCancelClick = onCancelClick
        ) {
            content()
        }
    }
}