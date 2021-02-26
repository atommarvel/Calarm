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

/**
 * TODO: animate screen navigation
 */
@Composable
fun App(activity: ComponentActivity) {
    CompositionLocalProvider(
        LocalPermissionsUtil provides PermissionsUtil(activity),
        LocalNavController provides rememberNavController()
    ) {
        NavHost(LocalNavController.current, EventsScreen.route) {
            composableScreen(EventsScreen)
            composableScreen(PermissionsScreen)
            composableScreen(CalendarSelectionScreen)
            composableScreen(SettingsScreen)
        }
    }
}