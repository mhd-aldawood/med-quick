package com.example.kotlintest.features_autentication.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kotlintest.util.scalePxToDp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
    bottom: (@Composable () -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier =  modifier
            .width(scalePxToDp(960f))
            .height(scalePxToDp(1069f))
    ) {
        Column(
            modifier = Modifier.padding(start = scalePxToDp(60f), end = scalePxToDp(60f), top = scalePxToDp(125f)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HEADER (fixed height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(scalePxToDp(85f))
            ) {
                header()
            }

            // CONTENT grows to fill all space if bottom is null
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ) {
                content()
            }

            // BOTTOM (only shown when provided)
            if (bottom != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(scalePxToDp(185f))
                ) {
                    bottom()
                }
            }
        }
    }
}