package com.example.kotlintest.screens.stethoscope.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.kotlintest.component.CardWithShadowOnBorder
import com.example.kotlintest.component.HorizontalSpacer
import com.example.kotlintest.screens.stethoscope.StethoScopeState
import com.example.kotlintest.screens.stethoscope.models.DeleteBtnStatus
import com.example.kotlintest.screens.stethoscope.models.PlayBtnStatus
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.util.horizontalPadding

@Composable
fun RowScope.RecordSections(
    uiState: StethoScopeState,
    onPlayClicked: (String?, playIcon: PlayBtnStatus) -> Unit,
    onDeleteClicked: (String?, DeleteBtnStatus) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
    ) {
        items(items = uiState.auscultationRecordList, key = { it.id })
        { it ->
            CardWithShadowOnBorder(modifier = Modifier
                .wrapContentWidth()
                .padding(bottom = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = it.title,
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .horizontalPadding(20),
                        style = MaterialTheme.typography.rhDisplayBold.copy(
                            color = PrimaryMidLinkColor,
                            fontSize = 13.sp
                        ), textAlign = TextAlign.Center
                    )
                    AudioWaveform(
                        samples = it.heartWave,
                        modifier = Modifier
                            .weight(1f),
                        barColor = Periwinkle,
                        minBarHeight = 10.dp
                    )
                    HorizontalSpacer(25)
                    Text(
                        text = it.duration,
                        style = MaterialTheme.typography.rhDisplayBold.copy(
                            fontSize = 10.sp,
                            color = Periwinkle
                        )
                    )
                    HorizontalSpacer(30)
                    when (it.isCashed) {
                        false -> Icon(
                            painterResource(it.recordingIcon),
                            modifier = Modifier.width(120.dp),
                            contentDescription = null, tint = Color.Unspecified
                        )

                        else -> {
                            Icon(
                                painterResource(it.playIcon.value),
                                null,
                                modifier = Modifier
                                    .width(50.dp)
                                    .clickable { onPlayClicked(it.file, it.playIcon) },
                                tint = Color.Unspecified
                            )
                            HorizontalSpacer(40)
                            Icon(
                                painterResource(it.deleteBtn.value),
                                null,
                                modifier = Modifier
                                    .width(50.dp)
                                    .clickable { onDeleteClicked(it.file, it.deleteBtn) },
                                tint = Color.Unspecified
                            )
                        }
                    }
                    HorizontalSpacer(40)

                }
            }

        }
    }
}
