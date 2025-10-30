package com.example.kotlintest.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermissionGateSimple(
    permissions: List<String>,
    rationaleText: String = "We need these permissions to continue.",
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    var requestAttempts by remember { mutableStateOf(0) }
    var latestResults by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        latestResults = results
        requestAttempts++
    }

    fun isGranted(perm: String): Boolean =
        ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED ||
                latestResults[perm] == true

    val allGranted = permissions.all(::isGranted)

    // Ask once when entering this composable
    LaunchedEffect(permissions) {
        if (!allGranted && requestAttempts == 0) {
            launcher.launch(permissions.toTypedArray())
        }
    }

    // Only render the content if every permission is granted
    if (allGranted) {
        content()
    }
}

// Helpers
private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
