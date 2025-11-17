package com.example.kotlintest.screens.poct.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayRegular

@Composable
fun CardListHeader(headerTitle: String, headerDate: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Text(
            text = headerTitle,
            style = MaterialTheme.typography.rhDisplayBold.copy(
                color = PrimaryMidLinkColor,
                fontSize = 16.sp
            )
        )
        Text(
            text = headerDate,
            style = MaterialTheme.typography.rhDisplayRegular.copy(
                color = PrimaryMidLinkColor,
                fontSize = 16.sp
            )
        )
    }
}
