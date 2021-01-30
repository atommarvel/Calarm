package com.radiantmood.calarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.radiantmood.calarm.CalendarRepository.UserCal

@Composable
fun CalendarsActivityScreen(navController: NavController, vm: MainViewModel) {
    Permissions.current.checkPermission(navController)
    val calendars: List<CalendarDisplay> by vm.calendarDisplays.observeAsState(listOf())
    vm.getCalendarDisplays()

    Column {
        TopAppBar(title = { Text("Select Calendars to use") }, actions = {
            // TODO: appbaricon
            IconButton(onClick = {
                // val selectedCalendars = calendarList.filter { selectedIds.contains(it.id) }
                // TODO: save calendar selection to a repository of some sort
                navController.popBackStack()
            }) {
                Icon(Icons.Default.Check, null)
            }
        })
        CalendarList(calendars) {
            vm.toggleSelectedCalendarId(it.userCal.id)
        }
    }
}

data class CalendarDisplay(val userCal: UserCal, val isSelected: Boolean)

@Composable
fun CalendarList(calendars: List<CalendarDisplay>, selectCalendar: (CalendarDisplay) -> Unit) {
    // TODO: loading view while waiting?
    LazyColumn {
        items(calendars) { calendar ->
            CalendarRow(calendar, selectCalendar)
            Divider()
        }
    }
}

@Composable
fun CalendarRow(calendar: CalendarDisplay, selectCalendar: (CalendarDisplay) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { selectCalendar(calendar) })
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = calendar.userCal.name, Modifier.weight(1f))
        Switch(checked = calendar.isSelected, onCheckedChange = {
            selectCalendar(calendar)
        })
    }
}