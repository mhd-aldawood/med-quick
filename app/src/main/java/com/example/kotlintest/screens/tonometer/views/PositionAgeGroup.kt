package com.example.kotlintest.screens.tonometer.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.component.VerticalSpacer
import com.example.kotlintest.screens.tonometer.models.AgeGroup
import com.example.kotlintest.screens.tonometer.models.AgeGroupBtn
import com.example.kotlintest.screens.tonometer.models.PositionType
import com.example.kotlintest.screens.tonometer.TonometerState
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold

@Composable
fun RowScope.PositionAgeGroup(
    onSittingPosChange: (PositionType) -> Unit,
    onAgeGroupChange: (AgeGroup) -> Unit = {},
    uiState: TonometerState,
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .weight(0.3f), horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        uiState.patientPositionList.forEachIndexed { index, info ->
            Icon(
                modifier = Modifier
                    .clickable
                    {
                        onSittingPosChange(info.type)
                    }
                    .size(65.dp),
                painter = painterResource(info.icon),
                contentDescription = "",
                tint = Color.Unspecified
            )
            if (index != uiState.patientPositionList.lastIndex) {
                VerticalSpacer(20)
            }
        }
        VerticalSpacer(20)
        uiState.ageGroupBtn.forEachIndexed { index, it ->
            AgeGroupButton(ageGroupBtn = it, onAgeGroupChange = onAgeGroupChange)
            if (index != uiState.ageGroupBtn.lastIndex) {
                VerticalSpacer(15)
            }
        }
    }
}

@Composable
fun AgeGroupButton(
    ageGroupBtn: AgeGroupBtn,
    onAgeGroupChange: (AgeGroup) -> Unit = {},
) {
    Text(
        text = ageGroupBtn.ageGroup.text,
        modifier = Modifier
            .background(
                color = ageGroupBtn.backgroundColor,
                shape = RoundedCornerShape(5.dp)
            )
            .border(1.dp, PrimaryMidLinkColor, RoundedCornerShape(5.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable {
                onAgeGroupChange(ageGroupBtn.ageGroup)
            }
            .width(65.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.rhDisplayBold.copy(
            fontSize = 10.sp,
            color = ageGroupBtn.ageGroupTextColor
        )
    )
}