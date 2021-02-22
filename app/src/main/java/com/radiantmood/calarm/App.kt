package com.radiantmood.calarm

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.radiantmood.calarm.util.InitNavHost
import com.radiantmood.calarm.util.PermissionsUtil

val LocalPermissionsUtil = compositionLocalOf<PermissionsUtil> { error("No PermissionsKit") }
val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController") }
val LocalAppBarTitle = compositionLocalOf<String> { error("No AppBarTitle") }

@Composable
fun App(activity: ComponentActivity) {
    val permissions = PermissionsUtil(activity)
    val navController = rememberNavController()
    Providers(
        LocalPermissionsUtil provides permissions,
        LocalNavController provides navController
    ) {
        InitNavHost()
    }
}