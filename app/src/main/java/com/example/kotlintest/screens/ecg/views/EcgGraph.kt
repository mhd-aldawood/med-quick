package com.example.kotlintest.screens.ecg.views

import android.graphics.PixelFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kotlintest.screens.ecg.model.ReviewWaveController
import com.example.kotlintest.ui.theme.locals.LocalReviewWaveFactory


@Composable
fun RowScope.EcgGraph( controller: ReviewWaveController) {

    val factory = LocalReviewWaveFactory.current
    Column(
        modifier = Modifier
            .background(color = Color.Gray)
            .weight(1f)
    ) {
        AndroidView(
            factory = { context ->
                factory.create(null).apply {
                    setZOrderOnTop(true)
                    holder.setFormat(PixelFormat.TRANSLUCENT)
                    setAmplitudeScale(0.5f);

                }.also { controller.attach(it) }
            }
            , update = {

            }
        )
    }
}