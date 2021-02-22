package com.radiantmood.calarm.util

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.screen.PermissionScreen
import com.radiantmood.calarm.screen.SettingsActivityScreen
import com.radiantmood.calarm.screen.calendars.CalendarsSelectionActivityScreen
import com.radiantmood.calarm.screen.events.EventsActivityScreen


@Composable
fun InitNavHost() {
    val navController = LocalNavController.current
    NavHost(navController, "alarms") {
        composable("alarms") { EventsActivityScreen() }
        composable("permission") { PermissionScreen() }
        composable("calendars") { CalendarsSelectionActivityScreen() }
        composable("settings") { SettingsActivityScreen() }
    }
}