package com.example.kotlintest.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.core.model.CardResult
import com.example.kotlintest.ui.theme.CeruleanBlue
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.util.horizontalPadding

@Composable
fun DataCardSection(
    cardHeader: List<String>,
    cardResult: List<CardResult>,
    id: String? = null,
    date: String? = null,
    idTextStyle: TextStyle? = null,
    dateTextStyle: TextStyle? = null,
    idRowModifier: Modifier,
    titleTextStyle: TextStyle,
    bodyTextStyle: TextStyle,
) {
    CardWithShadowOnBorder(
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.Start
        )
        {
            if (id != null && date != null) {
                Row(
                    modifier = idRowModifier.padding(bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = id, style = idTextStyle ?: MaterialTheme.typography.rhDisplayBlack)
                    Text(
                        text = date,
                        style = dateTextStyle ?: MaterialTheme.typography.rhDisplayBlack
                    )
                }
            }

            CardRow(
                cardHeader,
                paddingBottomValue = 10,
                textStyleList =
                    listOf(
                        titleTextStyle.copy(textAlign = TextAlign.Start),
                        titleTextStyle.copy(textAlign = TextAlign.Center),
                        titleTextStyle.copy(textAlign = TextAlign.Center),
                        titleTextStyle.copy(textAlign = TextAlign.Center),
                    )
            )

            cardResult.forEach {
                CardRow(
                    it.result,
                    paddingBottomValue = 5,
                    textStyleList =
                        listOf(
                            bodyTextStyle.copy(textAlign = TextAlign.Start),
                            bodyTextStyle.copy(textAlign = TextAlign.Center),
                            bodyTextStyle.copy(textAlign = TextAlign.Center),
                            bodyTextStyle.copy(textAlign = TextAlign.Center),
                        )
                )
            }
        }
    }

}


@Composable
fun CardRow(
    list: List<String>, paddingBottomValue: Int,
    textStyleList: List<TextStyle>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingBottomValue.dp, start = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        list.forEachIndexed { index, it ->
            Text(text = it, style = textStyleList.get(index), modifier = Modifier.weight(1f))
        }
    }
}
