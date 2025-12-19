package com.example.kotlintest.features_home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.CeruleanBlue
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.PaleCerulean
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.deepDarkBlue
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.util.scalePxToDp

@Composable
fun FilterChipGroup() {
    val options = listOf(
        "All",
        "Confirmed",
        "Pending",
        "Finished"
    )

    var selected by remember { mutableStateOf("All") }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            FilterChipItem(
                text = option,
                selected = selected == option,
                onClick = { selected = option }
            )
        }
    }
}

@Composable
fun FilterChipItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    OutlinedButton(
        onClick =  {onClick()},
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected)CeruleanBlue else PaleCerulean,
            contentColor = Lotion,
            disabledContainerColor = Color.White,
            disabledContentColor = PrimaryMidLinkColor
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, deepDarkBlue),
        modifier = Modifier
            .height(scalePxToDp(67f))
            .width(when (text) {
                "All" -> scalePxToDp(104f)
                "Confirmed" -> scalePxToDp(226f)
                "Pending" -> scalePxToDp(200f)
                "Finished" -> scalePxToDp(196f)
                else -> {scalePxToDp(200f)}
            })

    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.rhDisplayBold.copy(
                color = if (selected) Lotion else PrimaryMidLinkColor ,
                fontSize = 13.sp
            )
        )

        Spacer(Modifier.width(5.dp))
        if (text != "All") {
            Icon(
                painter = painterResource(when (text) {
                    "All" -> R.drawable.ic_med_finished_icon
                    "Confirmed" -> R.drawable.ic_med_confirmed_icon
                    "Pending" -> R.drawable.ic_med_pending_icon
                    "Finished" -> R.drawable.ic_med_finished_icon
                    else -> {R.drawable.ic_med_finished_icon}}
                ),
                contentDescription = null,
                tint = Color.Unspecified ,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(when (text) {
                        "All" -> scalePxToDp(30f)
                        "Confirmed" -> scalePxToDp(22f)
                        "Pending" -> scalePxToDp(25f)
                        "Finished" -> scalePxToDp(30f)
                        else -> {scalePxToDp(30f)}
                    })
            )
        }

    }

}
