package com.radiantmood.calarm

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.radiantmood.calarm.screen.AlarmsScreen
import com.radiantmood.calarm.screen.CalendarsActivityScreen


@Composable
fun InitNavHost() {
    val navController = AmbientNavController.current
    NavHost(navController, "alarms") {
        composable("alarms") { AlarmsScreen() }
        composable("permission") { PermissionScreen() }
        composable("calendars") { CalendarsActivityScreen() }
    }
}