package com.example.kotlintest.screens.home.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.util.pixelsToDp

@Composable
fun DeviceListSection(
    deviceList: String,
    savedLocallyIcon: Int,
    savedLocally: String,
    options: List<String>,
    optionBtnClicked: (Int) -> Unit
) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = deviceList,
            style = MaterialTheme.typography.rhDisplayBlack.copy(
                color = PrimaryMidLinkColor,
                fontSize = 30.sp
            ),
        )
        Spacer(modifier = Modifier.width(pixelsToDp(63F)))
        SyncLocally(savedLocallyIcon, savedLocally)
        Spacer(modifier = Modifier.weight(1f))
        OptionBtnList(optionList = options) { index ->
            optionBtnClicked(index)
        }

    }
}
