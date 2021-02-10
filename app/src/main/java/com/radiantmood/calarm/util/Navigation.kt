package com.radiantmood.calarm.util

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.radiantmood.calarm.AmbientNavController
import com.radiantmood.calarm.screen.PermissionScreen
import com.radiantmood.calarm.screen.calendars.CalendarsActivityScreen
import com.radiantmood.calarm.screen.events.EventsScreen


@Composable
fun InitNavHost() {
    val navController = AmbientNavController.current
    NavHost(navController, "alarms") {
        composable("alarms") { EventsScreen() }
        composable("permission") { PermissionScreen() }
        composable("calendars") { CalendarsActivityScreen() }
    }
}