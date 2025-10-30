package com.example.kotlintest

import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import com.contec.bp.code.connect.ContecSdk
import com.example.kotlintest.core.PermissionManager
import com.example.kotlintest.di.ReviewWaveFactory
import com.example.kotlintest.screens.ecg.model.ReviewWaveController
import com.example.kotlintest.ui.theme.KotlinTestTheme
import com.example.kotlintest.ui.theme.locals.LocalContecSdk
import com.example.kotlintest.ui.theme.locals.LocalPermissionManager
import com.example.kotlintest.ui.theme.locals.LocalReviewWaveController
import com.example.kotlintest.ui.theme.locals.LocalReviewWaveFactory
import com.example.kotlintest.util.Logger
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //<editor-fold desc="request permission">
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    )
    { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            Logger.i("MainActivity", "Requested Granted")
        } else {
            Logger.i("MainActivity", "Requested Revoked")
        }
    }

    fun requestLocationPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
    //</editor-fold>


    @Inject
    lateinit var contecSdk: ContecSdk
    @Inject lateinit var reviewWaveFactory: ReviewWaveFactory
    @Inject lateinit var reviewWaveController: ReviewWaveController
    @Inject lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        enableEdgeToEdge()
//        requestLocationPermissions()
        setContent {
            KotlinTestTheme {
                CompositionLocalProvider(LocalContecSdk provides contecSdk,
                    LocalReviewWaveFactory provides reviewWaveFactory,
                    LocalReviewWaveController provides reviewWaveController,
                    LocalPermissionManager provides permissionManager
                ) {
                    InitNavGraph(startDestination = NavDestination.ECG_SCREEN)
                }
            }
        }
    }


}