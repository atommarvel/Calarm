package com.radiantmood.calarm

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

val AmbientPermissions = ambientOf<PermissionsKit> { error("No PermissionsKit") }
val AmbientMainViewModel = ambientOf<MainViewModel> { error("No MainViewModel") }
val AmbientNavController = ambientOf<NavHostController> { error("No NavController") }

@Composable
fun App(activity: ComponentActivity) {
    val permissions = PermissionsKit(activity)
    val vm: MainViewModel = viewModel()
    val navController = rememberNavController()
    Providers(AmbientPermissions provides permissions, AmbientMainViewModel provides vm, AmbientNavController provides navController) {
        InitNavHost()
    }
}