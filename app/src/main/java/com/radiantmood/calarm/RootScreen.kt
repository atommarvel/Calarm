package com.radiantmood.calarm

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.navigate

@Composable
fun RootScreen(navHostController: NavController) {
    val permissions = Permissions.current
    if (permissions.isCalendarPermissionGranted) {
        navHostController.navigate("permission")
    } else {
        navHostController.navigate("alarms")
    }
}