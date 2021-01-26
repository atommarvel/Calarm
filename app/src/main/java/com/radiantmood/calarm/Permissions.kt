package com.radiantmood.calarm

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.markodevcic.peko.Peko

class Permissions(private val activity: Activity) {
    val isCalendarPermissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED

    suspend fun requestPermission(permission: String) {
        Peko.requestPermissionsAsync(activity, permission)
    }
}