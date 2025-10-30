package com.example.kotlintest.core

// PermissionGate.kt
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun PermissionGate(
    permissions: List<String>,
    modifier: Modifier = Modifier,
    rationaleText: String = "We need these permissions to continue.",
    permanentlyDeniedText: String = "Permissions were denied with 'Don't ask again'. Open settings to enable them.",
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    var requestAttempts by remember { mutableStateOf(0) }

    // Track the latest results returned by the launcher
    var latestResults by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        latestResults = results
        requestAttempts++
    }

    // Helper: check current grant state
    fun isGranted(perm: String): Boolean =
        ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED ||
                latestResults[perm] == true

    val allGranted = permissions.all { isGranted(it) }

    // These two are evaluated only if not all granted
    val needsRationale = !allGranted && activity != null && permissions.any { perm ->
        !isGranted(perm) && ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)
    }

    val permanentlyDenied = !allGranted && activity != null && permissions.any { perm ->
        !isGranted(perm) && !ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)
    }

    // First pass: if not granted yet and we haven't tried requesting in this composition, request.
    LaunchedEffect(permissions) {
        if (!allGranted) {
            launcher.launch(permissions.toTypedArray())
        }
    }

    when {
        allGranted -> content()

        needsRationale -> PermissionMessage(
            modifier = modifier,
            text = rationaleText,
            primary = "Grant permissions",
            onPrimary = { launcher.launch(permissions.toTypedArray()) },
            secondary = null,
            onSecondary = null
        )

        permanentlyDenied -> PermissionMessage(
            modifier = modifier,
            text = permanentlyDeniedText,
            primary = "Open Settings",
            onPrimary = { context.openAppSettings() },
            secondary = "Request again",
            onSecondary = { launcher.launch(permissions.toTypedArray()) }
        )

        else -> PermissionMessage(
            modifier = modifier,
            text = "Permissions required.",
            primary = "Continue",
            onPrimary = { launcher.launch(permissions.toTypedArray()) },
            secondary = null,
            onSecondary = null
        )
    }
}

@Composable
private fun PermissionMessage(
    modifier: Modifier = Modifier,
    text: String,
    primary: String,
    onPrimary: () -> Unit,
    secondary: String?,
    onSecondary: (() -> Unit)?
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onPrimary) { Text(primary) }
                if (secondary != null && onSecondary != null) {
                    OutlinedButton(onClick = onSecondary) { Text(secondary) }
                }
            }
        }
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
