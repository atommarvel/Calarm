package com.radiantmood.calarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.radiantmood.calarm.CalendarRepository.UserCal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun fetchCalendars(): List<UserCal>? = composableFetch {
    val calRepo = CalendarRepository()
    withContext(Dispatchers.Default) { calRepo.queryCalendars() }
}

@Composable
fun CalendarsScreen(navController: NavController) {
    Permissions.current.checkPermission(navController)
    val selectedIds = remember { mutableStateListOf<Int>() }
    fetchCalendars()?.let { calendarList ->
        Column {
            TopAppBar(title = { Text("Select Calendars to use") }, actions = {
                IconButton(onClick = {
                    val selectedCalendars = calendarList.filter { selectedIds.contains(it.id) }
                    // TODO: save calendar selection to a repository of some sort
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Default.Check)
                }
            })
            CalendarList(calendarList, selectedIds) {
                selectedIds.toggle(it.id)
            }
        }
    }
    // TODO: loading view while waiting?
}

@Composable
fun CalendarList(calendarList: List<UserCal>, selectedIds: List<Int>, selectCalendar: (UserCal) -> Unit) {
    LazyColumn {
        items(calendarList) { calendar ->
            CalendarRow(calendar, selectedIds.contains(calendar.id), selectCalendar)
            Divider()
        }
    }
}

@Composable
fun CalendarRow(calendar: UserCal, isSelected: Boolean, selectCalendar: (UserCal) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { selectCalendar(calendar) })
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = calendar.name, Modifier.weight(1f))
        Switch(checked = isSelected, onCheckedChange = {
            selectCalendar(calendar)
        })
    }
}