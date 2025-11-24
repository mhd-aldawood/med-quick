package com.example.kotlintest

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.example.kotlintest.core.PermissionManager
import com.example.kotlintest.di.ReviewWaveFactory
import com.example.kotlintest.screens.ecg.model.ReviewWaveController
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.ui.theme.locals.LocalPermissionManager
import com.example.kotlintest.ui.theme.locals.LocalReviewWaveController
import com.example.kotlintest.ui.theme.locals.LocalReviewWaveFactory
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var reviewWaveFactory: ReviewWaveFactory
    @Inject lateinit var reviewWaveController: ReviewWaveController
    @Inject lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        enableEdgeToEdge()
        setContent {
            KotlinTestTheme {
                CompositionLocalProvider(
                    LocalReviewWaveFactory provides reviewWaveFactory,
                    LocalReviewWaveController provides reviewWaveController,
                    LocalPermissionManager provides permissionManager
                ) {
                    InitNavGraph(startDestination = NavDestination.HOME_SCREEN)
                }
            }
        }
    }


}