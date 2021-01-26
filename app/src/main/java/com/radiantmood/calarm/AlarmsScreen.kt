package com.radiantmood.calarm

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.navigate

@Composable
fun AlarmsScreen(navController: NavController) {
    Column {
        TopAppBar(title = { Text("Today's Alarms") }, actions = {
            AppBarAction(drawableRes = R.drawable.ic_baseline_calendar_today_24) {
                navController.navigate("calendars")
            }
        })
        Fullscreen {
            Text("Alarms go here")
        }
    }
}