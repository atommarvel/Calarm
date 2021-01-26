package com.radiantmood.calarm

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticAmbientOf
import androidx.navigation.compose.rememberNavController

val Permissions = staticAmbientOf<PermissionsKit> { error("No permissions") }

@Composable
fun App(activity: Activity) {
    Permissions.provides(PermissionsKit(activity))

    val navController = rememberNavController()
    getNavHost(navController = navController)
}