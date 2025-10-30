package com.example.kotlintest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundedText(
    text: String="hello",
    backgroundColor: Color = Color.Blue,
    borderColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp, bottom = 50.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier.background(color = Color.Red).wrapContentWidth()
            ) {
                Text("Left Column")
            }

            Column(
                modifier = Modifier.background(color = Color.Gray).weight(1f)
            ) {
                Text("Right Column takes remaining space")
            }
        }
    }
}

@Preview
@Composable
fun Test(){
    RoundedText()
}