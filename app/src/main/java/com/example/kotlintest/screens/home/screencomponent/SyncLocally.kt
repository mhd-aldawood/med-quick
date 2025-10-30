package com.example.kotlintest.screens.home.screencomponent

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.LavenderGray
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.util.pixelsToDp


@Composable
fun RowScope.SyncLocally(savedLocallyIcon: Int, savedLocally: String) {
    Icon(
        modifier = Modifier.size(50.dp).padding(vertical = 10.dp),
        painter = painterResource(savedLocallyIcon),
        contentDescription = "",
        tint = Color.Unspecified
    )
    Spacer(modifier = Modifier.width(pixelsToDp(29F)))
    Text(
        text = savedLocally,
        style = MaterialTheme.typography.rhDisplayMedium.copy(
            color = LavenderGray,
            fontSize = 15.sp
        )
    )
}