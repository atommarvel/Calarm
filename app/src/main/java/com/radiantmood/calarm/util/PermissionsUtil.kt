package com.radiantmood.calarm.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.markodevcic.peko.Peko

class PermissionsUtil(private val activity: Activity) {

    suspend fun requestPermission(permission: String) {
        Peko.requestPermissionsAsync(activity, permission)
    }

    private fun isCalendarPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED

    fun checkPermission(navController: NavController) {
        if (!isCalendarPermissionGranted()) {
            navController.navigate("permission")
        }
    }
}