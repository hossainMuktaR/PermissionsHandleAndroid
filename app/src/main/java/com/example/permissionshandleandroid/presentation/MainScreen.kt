package com.example.permissionshandleandroid.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.permissionshandleandroid.openAppSettings
import kotlin.math.log


@Composable
fun MainScreen() {
    var isShowDialog by remember {
        mutableStateOf(false)
    }
    val permissionCamera = Manifest.permission.CAMERA
    val context = LocalContext.current as Activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )
    LaunchedEffect(key1 = true) {
        if (ContextCompat.checkSelfPermission(
            context,
            permissionCamera
        ) == PackageManager.PERMISSION_DENIED ) {
            launcher.launch(
                permissionCamera
            )
    }
    }

    if (isShowDialog) {
        PermissionDialog(
            onDismiss = {
                isShowDialog = false
            },
            onGoToAppSettingsClick = {
                context.openAppSettings()
                isShowDialog = false
            }
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                when (val action = getPermission(context, permissionCamera)) {
                    is PermissionRequestResult.Granted -> {
                        Toast.makeText(context, "Already Granted", Toast.LENGTH_SHORT).show()
                    }

                    is PermissionRequestResult.ShowRationale -> {
                        isShowDialog = true
                    }

                    is PermissionRequestResult.Request -> {
                        Toast.makeText(context, "Permission Requested", Toast.LENGTH_SHORT).show()
                        launcher.launch(
                            action.permission
                        )
                    }
                }
            }
        ) {
            Text(text = "Camera Permission")
        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = { /*TODO*/ }
//        ) {
//            Text(text = "Multiple Permissions")
//        }
    }


}

fun getPermission(activity: Activity, permissionName: String): PermissionRequestResult {
    return when {
        ContextCompat.checkSelfPermission(
            activity,
            permissionName
        ) == PackageManager.PERMISSION_GRANTED -> {
            PermissionRequestResult.Granted(permissionName)
        }

        !shouldShowRequestPermissionRationale(
            activity,
            permissionName
        ) -> {
            PermissionRequestResult.ShowRationale(permissionName)
        }

        else -> {
            PermissionRequestResult.Request(permissionName)
        }
    }
}

sealed class PermissionRequestResult(permission: String) {
    data class Granted(val permission: String) : PermissionRequestResult(permission)
    data class ShowRationale(val permission: String) : PermissionRequestResult(permission)
    data class Request(val permission: String) : PermissionRequestResult(permission)
}