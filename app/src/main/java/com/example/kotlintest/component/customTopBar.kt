package com.example.kotlintest.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.Platinum
import com.example.kotlintest.ui.theme.rhDisplayRegular
import com.example.kotlintest.util.scalePxToDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    onLeftClick: () -> Unit,
    onCenterIconClick: ((Int) -> Unit)? = null,
    rightText: String,
    icons: List<Int>? = null
) {
    TopAppBar(
        navigationIcon = {
            Box(modifier = Modifier.fillMaxHeight()) {
                Icon(
                    painter = painterResource(R.drawable.ic_med_link_logo),
                    contentDescription = "Menu",
                    tint = Color.Unspecified,
                    modifier = Modifier
//                        .padding(start = 50.dp)
                        .padding(start = scalePxToDp(100f))
                        .align(Alignment.Center)
                        .clickable { onLeftClick.invoke() }
                )
            }
        },
        title = {
            // Center group of 5 icons
            var selectedIndex by rememberSaveable { mutableStateOf(2) }
            icons.let {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    it?.forEachIndexed { index, icon ->
                        val horizontalPadding = if (index == 2) 5.dp else 15.dp

                        Icon(
                            painter = painterResource(icon),
                            contentDescription = "Center icon $index",
                            tint = if (index == selectedIndex) Platinum else Lotion,
                            modifier = Modifier
                                .padding(horizontal = horizontalPadding)
                                .size(24.dp)
                                .clickable {
                                    selectedIndex = index
                                    onCenterIconClick?.invoke(index)
                                }
                        )
                        if (index == 2) {
                            Text(
                                text = "Examination",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.rhDisplayRegular.copy(color = Lotion),
                                modifier = Modifier
                                    .padding(end = 25.dp)

                            )
                        }

                    }
                }
            }


        },
        actions = {
            Box (modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = rightText,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme
                        .typography
                        .rhDisplayRegular
                        .copy(color = Platinum),
                    modifier = Modifier
                        .padding(end = scalePxToDp(100f))
//                        .padding(end = 50.dp)
                        .align(Alignment.Center)
                )
            }

        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = PrimaryMidLinkColor
            ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
    )
}
