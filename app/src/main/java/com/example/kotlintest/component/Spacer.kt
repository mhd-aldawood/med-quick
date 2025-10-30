package com.example.kotlintest.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kotlintest.util.pixelsToDp

@Composable
fun VerticalSpacer(value:Int){
    Spacer(modifier = Modifier.height(pixelsToDp(value.toFloat())))
}
@Composable
fun HorizontalSpacer(value:Int){
    Spacer(modifier = Modifier.width(pixelsToDp(value.toFloat())))
}