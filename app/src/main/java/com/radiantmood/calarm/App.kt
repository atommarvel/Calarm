package com.radiantmood.calarm

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.radiantmood.calarm.util.PermissionsUtil

val LocalPermissionsUtil = compositionLocalOf<PermissionsUtil> { error("No PermissionsKit") }
val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController") }
val LocalAppBarTitle = compositionLocalOf<String> { error("No AppBarTitle") }

@Composable
fun App(activity: ComponentActivity) {
    val permissions = PermissionsUtil(activity)
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalPermissionsUtil provides permissions,
        LocalNavController provides navController
    ) {
        val navController = LocalNavController.current //TODO: Screens
        NavHost(navController, EventsScreen.route) {
            composableScreen(EventsScreen)
            composableScreen(PermissionsScreen)
            composableScreen(CalendarSelectionScreen)
            composableScreen(SettingsScreen)
        }
    }
}