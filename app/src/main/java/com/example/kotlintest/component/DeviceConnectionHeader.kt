package com.example.kotlintest.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayBold

@Composable
fun DeviceConnectionHeader(
    titleIcon: Int,
    title: String,
    cancelText: String,
    cancelIcon: Int,
    onCancelClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Row(
            modifier = Modifier.align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        )
        {
            Icon(
                painter = painterResource(titleIcon),
                tint = Color.Unspecified,
                modifier = Modifier.size(30.dp),
                contentDescription = ""
            )
            Text(
                text = title,
                style = MaterialTheme.typography.rhDisplayBlack.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 30.sp
                )
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable { onCancelClick.invoke() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(21.dp)
        )
        {

            Text(
                text = cancelText,
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = Periwinkle,
                    fontSize = 17.sp
                )
            )

            Icon(
                painter = painterResource(cancelIcon),
                contentDescription = "", modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
        }
        Box(modifier = Modifier
            .align(Alignment.Center)
            .padding(top = 60.dp)) {
            content()
        }
    }
}