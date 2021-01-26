package com.radiantmood.calarm

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun InitNavHost(navController: NavHostController) {
    NavHost(navController, "alarms") {
        composable("alarms") { AlarmsScreen(navController) }
        composable("permission") { PermissionScreen(navController) }
        composable("calendars") {
            CalendarsScreen(calendarList = emptyList(), navController)
        } // TODO: obtain calendar list
    }
}