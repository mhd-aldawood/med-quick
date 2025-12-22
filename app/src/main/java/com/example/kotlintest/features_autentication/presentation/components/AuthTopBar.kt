package com.example.kotlintest.features_autentication.presentation.components


import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.ui.theme.Platinum
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayRegular
import com.example.kotlintest.util.scalePxToDp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopBar() {
    TopAppBar(
        navigationIcon = {
            Box(modifier = Modifier.fillMaxHeight()) {
                Icon(
                    painter = painterResource(R.drawable.ic_med_link_logo),
                    contentDescription = "Menu",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(start = scalePxToDp(100f))
                        .align(Alignment.Center)
                )
            }
        },
        title = {},
        actions = {
            Box (modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = "Demo Version 0.2",
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

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun DefaultPreview() {
    KotlinTestTheme {
        AuthTopBar()
    }
}