package com.example.kotlintest.screens.stethoscope.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.component.HorizontalSpacer
import com.example.kotlintest.screens.stethoscope.model.AuscultationSite
import com.example.kotlintest.ui.theme.ShadowBlue
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayMedium

@Composable
fun PickAuscultationSite(
    pickLocation: String,
    listOfAuscultationSite: List<AuscultationSite>,
    onCardClicked: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // This allows children to align vertically
    ) {
        Text(
            text = pickLocation.split(" ").joinToString("\n"),
            style = MaterialTheme.typography.rhDisplayMedium
                .copy(fontSize = 25.sp, color = ShadowBlue)
        )
        HorizontalSpacer(40)
        VerticalDivider(
            color = ShadowBlue,
            modifier = Modifier
                .fillMaxHeight()  // makes it match the tallest child
                .width(25.dp)
        )
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            listOfAuscultationSite.forEachIndexed { index, it ->
                ElevatedCard(
                    modifier = Modifier.clickable { onCardClicked(index) }, colors = CardDefaults
                        .cardColors(
                            containerColor = Color.White
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(250.dp)
                            .height(80.dp) // ⬅ same height for all
                            .background(it.backGroundColor, RoundedCornerShape(10.dp))
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it.text,
                            style = MaterialTheme.typography.rhDisplayBold.copy(
                                fontSize = 15.sp,
                                color = it.textColor,
                                textAlign = TextAlign.Center
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (it != listOfAuscultationSite.last())
                    HorizontalSpacer(20)
            }
        }


//                Box(
//                    modifier = Modifier
//                        .width(250.dp)
//                        .height(80.dp) // ⬅ same height for all
//                        .background(it.backGroundColor, RoundedCornerShape(10.dp))
//                        .border(2.dp, it.textColor, RoundedCornerShape(10.dp))
//                        .padding(horizontal = 30.dp, vertical = 10.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = it.text,
//                        style = MaterialTheme.typography.rhDisplayBold.copy(
//                            fontSize = 15.sp,
//                            color = it.textColor,
//                            textAlign = TextAlign.Center
//                        ),
//                        textAlign = TextAlign.Center
//                    )
//                }
//                if (it != listOfAuscultationSite.last())
//                    HorizontalSpacer(20)
    }

}

