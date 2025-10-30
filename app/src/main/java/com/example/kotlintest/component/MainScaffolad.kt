package com.example.kotlintest.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlintest.util.pixelsToDp
import com.example.kotlintest.util.pxToDp
import com.example.kotlintest.util.scalePxToDp
import com.example.kotlintest.util.toDpFromXd

@Composable
fun MainScaffold(
    onLeftClick: () -> Unit = {},
    onCenterIconClick: ((Int) -> Unit)? = null,
    rightText: String = "Demo Version 0.2",
    icons: List<Int>? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onLeftClick = onLeftClick,
                onCenterIconClick = onCenterIconClick,
                rightText = rightText,
                icons = icons
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
//                    .padding(innerPadding)
//                    .padding(horizontal = 50.dp)
//                    .padding(top = pixelsToDp(60F)),
                    .padding(
                        start = scalePxToDp(87f),
                        end = scalePxToDp(60f),
                        top = scalePxToDp(252f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                content(innerPadding)
            }
        },
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    )
}
