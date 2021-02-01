package com.radiantmood.calarm.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.markodevcic.peko.Peko

class PermissionsUtil(private val activity: Activity) {

    suspend fun requestPermission(permission: String) {
        Peko.requestPermissionsAsync(activity, permission)
    }

    fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + activity.packageName)
        )
        ActivityCompat.startActivityForResult(activity, intent, 777, null)
    }

    fun arePermissionsGranted(): Boolean = isCalendarPermissionGranted() && isOverlayPermissionGranted()

    fun isCalendarPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED

    fun isOverlayPermissionGranted(): Boolean = Settings.canDrawOverlays(activity)

    fun checkPermissions(navController: NavController): Boolean = if (!arePermissionsGranted()) {
        navController.navigate("permission")
        true
    } else false
}