package com.example.kotlintest.screens.stethoscope.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconSections(
    recordIcon: Int,
    checkIcon: Int,
    onRecordClick: () -> Unit,
    onCheckClick: () -> Unit
) {
    Column(modifier = Modifier.wrapContentWidth()) {
        Icon(
            painter = painterResource(recordIcon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(75.dp)
                .padding(8.dp)
                .clickable { onRecordClick.invoke() })
        Icon(
            painter = painterResource(checkIcon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(75.dp)
                .scale(1.15f)
                .clickable { onCheckClick.invoke() })

    }
}