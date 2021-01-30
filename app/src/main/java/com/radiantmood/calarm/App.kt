package com.radiantmood.calarm

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.navigation.compose.rememberNavController

val Permissions = ambientOf<PermissionsKit> { error("No permissions") }

@Composable
fun App(activity: ComponentActivity) {
    val vm by activity.viewModels<MainViewModel>()
    val permissions = PermissionsKit(activity)
    val navController = rememberNavController()
    Providers(Permissions provides permissions) {
        InitNavHost(navController, vm)
    }
}