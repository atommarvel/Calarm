package com.radiantmood.calarm

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.radiantmood.calarm.util.InitNavHost
import com.radiantmood.calarm.util.PermissionsUtil

val LocalPermissionsUtil = compositionLocalOf<PermissionsUtil> { error("No PermissionsKit") }
val LocalMainViewModel = compositionLocalOf<MainViewModel> { error("No MainViewModel") }
val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController") }
val LocalAppBarTitle = compositionLocalOf<String> { error("No AppBarTitle") }

@Composable
fun App(activity: ComponentActivity) {
    val permissions = PermissionsUtil(activity)
    val vm: MainViewModel = viewModel()
    val navController = rememberNavController()
    Providers(
        LocalPermissionsUtil provides permissions,
        LocalMainViewModel provides vm,
        LocalNavController provides navController
    ) {
        InitNavHost()
    }
}