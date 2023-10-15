package com.example.permissionshandleandroid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.permissionshandleandroid.presentation.MainScreen
import com.example.permissionshandleandroid.ui.theme.PermissionsHandleAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionsHandleAndroidTheme {
                MainScreen()
            }
        }
    }
}
fun Activity.openAppSettings() {
    Intent(
       Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also { startActivity(it) }
}