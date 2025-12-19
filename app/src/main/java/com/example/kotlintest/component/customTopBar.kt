package com.example.kotlintest.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.Platinum
import com.example.kotlintest.ui.theme.White
import com.example.kotlintest.ui.theme.rhDisplayRegular
import com.example.kotlintest.util.scalePxToDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    onLeftClick: () -> Unit,
    onCenterIconClick: ((Int) -> Unit)? = null,
    rightText: String,
    icons: List<Int>? = null,
    titles : List<String>? =null
) {
    TopAppBar(
        navigationIcon = {
            Box(modifier = Modifier.fillMaxHeight(),
                ) {
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
            var selectedIndex by rememberSaveable { mutableStateOf(0) }
            icons.let {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    it?.forEachIndexed { index, icon ->
                        val horizontalPadding = if (index == selectedIndex) 5.dp else 15.dp
                        Column( modifier = Modifier.width(IntrinsicSize.Min)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = "Center icon $index",
                                    tint = if (index == selectedIndex) White else Lotion,
                                    modifier = Modifier
                                        .padding(horizontal = horizontalPadding)
                                        .size(scalePxToDp(48f))
                                        .clickable {
                                            selectedIndex = index
                                            onCenterIconClick?.invoke(index)
                                        }
                                )
                                if ( index ==selectedIndex) {
                                    Text(
                                        text = titles?.get(selectedIndex) ?: "",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.rhDisplayRegular.copy(color = Lotion, fontSize = 24.sp),
                                        modifier = Modifier
                                            .padding(start = 10.dp, end = 10.dp)
                                            .align(alignment = Alignment.Bottom),

                                    )
                                }
                            }
                            if (index ==selectedIndex)
                            {
                                //Spacer(Modifier.height(5.dp))
                                HorizontalDivider(
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(start = 5.dp, end = 10.dp),
                                    color = White,
                                    thickness = scalePxToDp(9f)
                                )

                            }

                            else
                            {
                                Spacer(Modifier.height(5.dp))
                                Box(
                                    modifier = Modifier.width(scalePxToDp(10f))
                                        .height(scalePxToDp(9f))
                                        .background(Lotion)
                                        .align(Alignment.CenterHorizontally)){}
                            }


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
            .height(60.dp)
            .clip(shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
    )
}
