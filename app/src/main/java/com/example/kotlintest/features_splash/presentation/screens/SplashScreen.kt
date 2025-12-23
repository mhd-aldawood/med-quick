package com.example.kotlintest.features_splash.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kotlintest.NavDestination
import com.example.kotlintest.R
import com.example.kotlintest.util.data.model.AppAuthState
import com.example.kotlintest.features_splash.presentation.viewmodel.SplashViewModel
import com.example.kotlintest.navigation.safeNavigate
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.util.scalePxToDp

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel =  hiltViewModel()) {

    val splashScreenState by viewModel.splashScreenState .collectAsState()

    LaunchedEffect(splashScreenState.appAuthState) {
        when (splashScreenState.appAuthState) {
            AppAuthState.LogedIn -> {
                navController.safeNavigate(NavDestination.CALENDAR_SCREEN)
            }
            AppAuthState.FirstTime -> {
                navController.safeNavigate(NavDestination.AUTH_SCREEN)
            }
            else -> {
                // do nothing for Loading or default state
            }
        }
    }


    // UI
    Box(
        modifier = Modifier.fillMaxSize()
            .background(PrimaryMidLinkColor),
        contentAlignment = Alignment.Center,

    ) {
        Image(
            painter = painterResource(R.drawable.ic_med_link_logo),
            contentDescription = null,
            modifier = Modifier.width(scalePxToDp(570f))
                .height(scalePxToDp(200f)),
            contentScale = ContentScale.FillBounds

        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun SplashScreenPreview() {
    KotlinTestTheme {
        SplashScreen(
            navController = NavController(LocalContext.current)
        )
    }
}