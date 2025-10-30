package com.example.kotlintest.screens.home.screencomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineBreak.Companion.Simple
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.screens.home.HomeScreenCard
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayRegular


@Composable
fun ColumnScope.DevicesSection(cardList: List<HomeScreenCard>, onCardClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 👈 3 columns
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        itemsIndexed(cardList) { index, item ->
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable { onCardClick(index) }) {
                ElevatedCard(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 15.dp)
                        .align(Alignment.TopCenter),
                    colors = CardDefaults
                        .cardColors(
                            containerColor = Color.White
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 90.dp)
                            .padding(horizontal = 25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Text(
                            text = item.deviceCategory.title, style = MaterialTheme
                                .typography
                                .rhDisplayBold
                                .copy(color = YankeesBlue, fontSize = 15.sp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        item.services?.joinToString(separator = " . ")?.let {
                            Text(
                                text = it, style = MaterialTheme
                                    .typography
                                    .rhDisplayRegular
                                    .copy(
                                        color = PrimaryMidLinkColor,
                                        lineBreak = Simple,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = item.cardBottomOptions.title,
                            style = MaterialTheme
                                .typography
                                .rhDisplayBold
                                .copy(
                                    color = PrimaryMidLinkColor,
                                    lineBreak = Simple,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                )
                        )
                        Spacer(modifier = Modifier.height(15.dp))


                    }
                }
                Icon(
                    painter = painterResource(item.deviceIcon),
                    tint = Color.Unspecified, contentDescription = "",
                    modifier = Modifier
                        .size(65.dp)
                        .align(Alignment.TopCenter)
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 30.dp),
                ) {
                    item.connectionStateList.forEachIndexed { index, state ->
                        Icon(
                            painter = painterResource(state.type.getIcon(state.status)),
                            tint = Color.Unspecified, contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(11.dp))
                    }


                }
            }

        }
    }

}





