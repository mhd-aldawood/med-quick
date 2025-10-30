package com.example.kotlintest.core

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun PermissionGateMinimal(
    permissions: List<String>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    var allGranted by remember { mutableStateOf(false) }

    // Permission launcher
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        allGranted = results.all { it.value }
    }

    // Check on first composition
    LaunchedEffect(Unit) {
        val granted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (granted) {
            allGranted = true
        } else {
            launcher.launch(permissions.toTypedArray())
        }
    }

    if (allGranted) {
        content()
    } else {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Required permissions are not granted.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = { launcher.launch(permissions.toTypedArray()) }) {
                    Text("Grant permissions")
                }
            }
        }
    }
}

// helper to get activity from context
private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
