package com.example.kotlintest.features_appointment.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.White
import com.example.kotlintest.util.scalePxToDp

@Composable
fun CustomThreeStateSwitch(
    state: SwitchState,
    onStateChange: (SwitchState) -> Unit,
    trackWidth: Dp = scalePxToDp(110f),
    trackHeight: Dp = scalePxToDp(55f),
    thumbSize: Dp = scalePxToDp(45f),
    trackColor: Color = White,
    thumbColorOn: Color = PrimaryMidLinkColor,
    thumbColorOff: Color = Periwinkle,
    borderWidth: Dp = 1.dp,
    borderColor: Color = PrimaryMidLinkColor
) {
    val padding = (trackHeight - thumbSize) / 2

    val maxOffset = trackWidth - thumbSize - padding * 2

    val thumbOffset by animateDpAsState(
        targetValue = when (state) {
            SwitchState.LEFT -> 1.dp
            SwitchState.CENTER -> maxOffset / 2
            SwitchState.RIGHT -> maxOffset
        },
        label = ""
    )

    Box(
        modifier = Modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(RoundedCornerShape(trackHeight / 2))
            .border(borderWidth, borderColor, RoundedCornerShape(trackHeight / 2))
            .background(trackColor)
            .clickable {
                val nextState = when (state) {
                    SwitchState.LEFT -> SwitchState.CENTER
                    SwitchState.CENTER -> SwitchState.RIGHT
                    SwitchState.RIGHT -> SwitchState.LEFT
                }
                onStateChange(nextState)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .padding(padding)
                .size(thumbSize)
                .clip(CircleShape)
                .background(if (state == SwitchState.CENTER) thumbColorOff else thumbColorOn )
        )
    }
}

enum class SwitchState {
    LEFT,
    CENTER,
    RIGHT
}
