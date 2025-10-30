package com.example.kotlintest.core

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionManager @Inject constructor(@ApplicationContext private val context: Context) {

    fun checkPermissionsAndExecute(
        permissions: List<String>,
        content: () -> Unit
    ) {
        if (arePermissionsGranted(permissions)) {
            content()  // Execute the content if all permissions are granted
        } else {
            showToast("Permissions required!")
        }
    }

    private fun arePermissionsGranted(permissions: List<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @Composable
    fun RequestPermissions(
        permissions: List<String>,
        onPermissionsResult: (Boolean) -> Unit
    ) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            val allGranted = result.all { it.value }
            onPermissionsResult(allGranted)
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
