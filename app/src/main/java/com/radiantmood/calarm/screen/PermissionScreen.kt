package com.radiantmood.calarm.screen

import android.Manifest.permission.READ_CALENDAR
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Text(text = "Missing Permissions", fontSize = 24.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        if (!permissionsUtil.isCalendarPermissionGranted()) {
            Text(text = "We need to be able to see calendar events to assign alarms to.", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onPermissionRequest(permissionsUtil, navController, READ_CALENDAR) }) {
                Text(text = "Grant Calendar Permission")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        if (!permissionsUtil.isOverlayPermissionGranted()) {
            Text(text = "We need to be show an alarm even if the phone screen is locked.", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            // TODO: re-render (via onResume?) after coming back from granting permission
            Button(onClick = { permissionsUtil.requestOverlayPermission() }) {
                Text(text = "Grant Overlay Permission")
            }
        }
    }
}

fun onPermissionRequest(permissionsUtil: PermissionsUtil, navController: NavController, permission: String) = GlobalScope.launch {
    permissionsUtil.requestPermission(permission)
    withContext(Dispatchers.Main) {
        navController.popBackStack()
    }
}