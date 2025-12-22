package com.example.kotlintest.features_appointment.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.util.scalePxToDp


@Composable
fun PaymentMethodItemCard(
    paymentMethodName: String = "eeee",
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.width(scalePxToDp(200f))
            .height(scalePxToDp(160f)),
    ) {
        CustomItemCard(
            modifier = Modifier.matchParentSize(),
            selected = selected,
            onClick = onClick,
            content = {
                PaymentMethodItemCardContent(
                    paymentMethodName = paymentMethodName,
                )
            }
        )
    }
}

@Composable
fun PaymentMethodItemCardContent(
    paymentMethodName: String = "",
)
{
    var iconId = R.drawable.ic_med_cash_icon
    var iconWidth = 60f
    when(paymentMethodName) {
        "Insurance" -> {
            iconId = R.drawable.ic_med_insurance_icon
            iconWidth = 60f
        }

        "Credit / Debit" -> {
            iconId = R.drawable.ic_med_credit_icon
            iconWidth = 100f
        }

        "Cash" -> {
            iconId = R.drawable.ic_med_cash_icon
            iconWidth = 60f
        }

        else -> {
            iconId = R.drawable.ic_med_cash_icon
            iconWidth = 60f
        }
    }

        Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 7.dp)
    ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(iconId),
                    contentDescription = null,
                    modifier = Modifier.height(scalePxToDp(40f))
                        .width(scalePxToDp(iconWidth)),
                    contentScale = ContentScale.Inside

                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = paymentMethodName,
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = YankeesBlue,
                        fontSize = 12.sp
                    )
                )


            }
            Spacer(modifier = Modifier.width(3.dp))

    }
}


@Preview
@Composable
fun PaymentMethodCardPreview() {
    PaymentMethodItemCard()
}