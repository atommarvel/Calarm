package com.radiantmood.calarm

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorResource

@Composable
fun AlarmsScreen(onClickCalendars: () -> Unit) {
    Column {
        TopAppBar(title = { Text("Today's Alarms") }, actions = {
            IconButton(onClick = onClickCalendars) { Icon(imageVector = vectorResource(R.drawable.ic_baseline_calendar_today_24)) }
        })
        Fullscreen {
            Text("Alarms go here")
        }
    }
}