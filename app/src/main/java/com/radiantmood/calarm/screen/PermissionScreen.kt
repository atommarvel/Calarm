package com.radiantmood.calarm.screen

import android.Manifest.permission.READ_CALENDAR
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.radiantmood.calarm.AmbientNavController
import com.radiantmood.calarm.AmbientPermissionsUtil
import com.radiantmood.calarm.util.Fullscreen
import com.radiantmood.calarm.util.PermissionsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun PermissionScreen() {
    val navController = AmbientNavController.current
    val permissionsUtil = AmbientPermissionsUtil.current

    Fullscreen {
        Text(text = "Permissions are required.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onPermissionRequest(permissionsUtil, navController, READ_CALENDAR) }) {
            Text(text = "Grant Calendar Permission")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { permissionsUtil.requestOverlayPermission() }) {
            Text(text = "Grant Overlay Permission")
        }
    }
}

fun onPermissionRequest(permissionsUtil: PermissionsUtil, navController: NavController, permission: String) = GlobalScope.launch {
    permissionsUtil.requestPermission(permission)
    if (permissionsUtil.arePermissionsGranted()) {
        withContext(Dispatchers.Main) {
            navController.popBackStack()
        }
    }
}