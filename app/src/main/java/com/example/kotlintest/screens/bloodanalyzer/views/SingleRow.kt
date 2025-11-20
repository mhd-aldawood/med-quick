package com.example.kotlintest.screens.bloodanalyzer.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.SpaceCadet

@Composable
fun SingleRow(item: String, result: String, units: String, reference: String, style: TextStyle) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = item,
            modifier = Modifier.weight(1f),
            style = style.copy(color = SpaceCadet, fontSize = 18.sp)
        )
        Text(
            text = result,
            modifier = Modifier.weight(1f),
            style = style.copy(color = SpaceCadet, fontSize = 18.sp)
        )
        Text(
            text = units,
            modifier = Modifier.weight(1f),
            style = style.copy(color = SpaceCadet, fontSize = 18.sp)
        )
        Text(
            text = reference,
            modifier = Modifier.weight(1f),
            style = style.copy(color = SpaceCadet, fontSize = 18.sp)
        )
    }
}