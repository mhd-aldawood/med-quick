package com.example.kotlintest.ui.theme.locals

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.contec.bp.code.connect.ContecSdk
import com.example.kotlintest.core.PermissionManager
import com.example.kotlintest.di.ReviewWaveFactory
import com.example.kotlintest.screens.ecg.model.ReviewWaveController

/*
* this file is used to provide the tonometer ContecSdk to the whole app
* since they want context to be used in there communicate method
*
* */
val LocalContecSdk = staticCompositionLocalOf<ContecSdk> {
    error("Tonometer ContecSdk not provided")
}

val LocalReviewWaveFactory = compositionLocalOf<ReviewWaveFactory> {
    error(" ReviewWaveFactory not provided")
}

val LocalReviewWaveController = compositionLocalOf<ReviewWaveController> {
    error(" ReviewWaveController not provided")
}
val LocalPermissionManager = compositionLocalOf<PermissionManager> {
    error(" LocalPermissionManager not provided")
}