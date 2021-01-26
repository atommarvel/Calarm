package com.radiantmood.calarm

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun getNavHost(navController: NavHostController, isCalendarPermissionGranted: Boolean) {
    NavHost(navController, "root") {
        composable("root") { RootScreen(navController) }
        composable("permission") { PermissionScreen() }
        composable("alarms") { AlarmsScreen(onClickCalendars = { /*TODO: use nav controller to handle this click*/ }) }
        composable("calendars") {
            CalendarsScreen(
                calendarList = emptyList(),
                finished = { /*TODO: use nav controller to go back*/ })
        } // TODO: obtain calendar list
    }
}