package com.example.kotlintest.screens.ecg.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kotlintest.component.DurationPicker

@Composable
fun DurationWithIcon(onDurationChange: (Int) -> Unit,icon:Int){
    Column(
        modifier = Modifier
            .wrapContentSize()
//        , verticalArrangement = Arrangement.SpaceBetween
    ) {
        DurationPicker() {
            onDurationChange(it)
        }
        Image(painter = painterResource(icon),
            contentDescription = "check",
            modifier=Modifier.
            sizeIn(maxWidth = 120.dp, maxHeight = 120.dp)
        )

    }
}