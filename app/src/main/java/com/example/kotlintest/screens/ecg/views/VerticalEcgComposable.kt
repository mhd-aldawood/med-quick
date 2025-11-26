package com.example.kotlintest.screens.ecg.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import java.util.concurrent.ConcurrentLinkedQueue

@Composable
fun RowScope.VerticalEcgComposable(
    ecgQueue: ConcurrentLinkedQueue<Short>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .background(color = Color.Gray)
            .weight(1f)
    ) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                VerticalECGView(context, ecgQueue)
            }
        )
    }
}
