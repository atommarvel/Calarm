package com.radiantmood.calarm

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.markodevcic.peko.Peko

class PermissionsKit(private val activity: Activity) {
    val isCalendarPermissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED

    suspend fun requestPermission(permission: String) {
        Peko.requestPermissionsAsync(activity, permission)
    }

    fun checkPermission(navController: NavController) {
        if (!isCalendarPermissionGranted) {
            navController.navigate("permission")
        }
    }
}