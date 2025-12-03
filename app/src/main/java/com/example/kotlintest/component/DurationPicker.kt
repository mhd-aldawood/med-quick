package com.example.kotlintest.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplaySemiBold
import kotlinx.coroutines.launch

@Composable
fun DurationPicker(
    modifier: Modifier = Modifier,
    durations: List<Int> = listOf(10, 15, 20, 30, 60),
    onDurationChange: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val itemHeight = 60.dp
    val density = LocalDensity.current

    var viewportHeightPx by remember { mutableStateOf(0) }

    // Which index is visually closest to center right now
    val selectedIndex by remember {
        derivedStateOf {
            val layout = listState.layoutInfo
            if (layout.visibleItemsInfo.isEmpty()) return@derivedStateOf -1
            val center = (layout.viewportStartOffset + layout.viewportEndOffset) / 2
            layout.visibleItemsInfo.minByOrNull { info ->
                val itemCenter = info.offset + info.size / 2
                kotlin.math.abs(itemCenter - center)
            }?.index ?: -1
        }
    }

    // Initial center on value 20
    LaunchedEffect(viewportHeightPx) {
        if (viewportHeightPx > 0) {
            val idx = durations.indexOf(20).coerceAtLeast(0)
            listState.scrollToItem(idx)     // rough place
            // hard-align to the visual center
            val layout = listState.layoutInfo
            val center = (layout.viewportStartOffset + layout.viewportEndOffset) / 2
            val info = layout.visibleItemsInfo.find { it.index == idx }
            if (info != null) {
                val itemCenter = info.offset + info.size / 2
                listState.scrollBy((itemCenter - center).toFloat())
            }
            onDurationChange(durations[idx])
        }
    }

    // Keep things perfectly centered while user scrolls (correct tiny drift)
    LaunchedEffect(selectedIndex, viewportHeightPx) {
        val layout = listState.layoutInfo
        val info = layout.visibleItemsInfo.find { it.index == selectedIndex } ?: return@LaunchedEffect
        val center = (layout.viewportStartOffset + layout.viewportEndOffset) / 2
        val itemCenter = info.offset + info.size / 2
        val delta = (itemCenter - center).toFloat()
        if (kotlin.math.abs(delta) > 0.5f) listState.scrollBy(delta)
    }

    Column(
        modifier = modifier
            .background(Color.Transparent, RoundedCornerShape(16.dp))
            .border(1.dp, PrimaryMidLinkColor, RoundedCornerShape(16.dp))
            .sizeIn(maxWidth = 120.dp, maxHeight = 300.dp)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Duration\n(sec)",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                fontSize = 17.sp, color = PrimaryMidLinkColor
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .onGloballyPositioned { viewportHeightPx = it.size.height },
            contentAlignment = Alignment.Center
        ) {
            // arrows overlay
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_left_arrow),
                    contentDescription = null,
                    tint = Color(0xFF0D47A1),
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(90f)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_right_arrow),
                    contentDescription = null,
                    tint = Color(0xFF0D47A1),
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(90f)
                )
            }

            // precise top/bottom padding so "start at index" equals "centered"
            val verticalPadding = with(density) {
                val halfViewport = viewportHeightPx / 2f
                val halfItem = itemHeight.toPx() / 2f
                maxOf(0f, halfViewport - halfItem).toDp()
            }

            LazyColumn(
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = verticalPadding),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(durations, key = { _, item -> item }) { index, item ->
                    val isCenter = index == selectedIndex

                    // fixed layout box, text scales inside without changing row height
                    Box(
                        modifier = Modifier
                            .height(itemHeight)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val scale by animateFloatAsState(if (isCenter) 1.5f else 1f, label = "scale")
                        val color by animateColorAsState(
                            if (isCenter) Color(0xFF0D47A1) else Color(0xFFB0BEC5),
                            label = "color"
                        )
                        Text(
                            text = item.toString(),
                            color = color,
                            fontSize = if (isCenter) 26.sp else 18.sp,
                            fontWeight = if (isCenter) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .graphicsLayer {
                                    // scale visually; Box keeps the row height constant
                                    scaleX = scale
                                    scaleY = scale
                                }
                                .clickable {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index)
                                        onDurationChange(item)
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}
