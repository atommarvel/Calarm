package com.radiantmood.calarm

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun InitNavHost() {
    val navController = AmbientNavController.current
    NavHost(navController, "alarms") {
        composable("alarms") { AlarmsScreen() }
        composable("permission") { PermissionScreen() }
        composable("calendars") { CalendarsActivityScreen() }
    }
}