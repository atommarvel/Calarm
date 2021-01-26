package com.radiantmood.calarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalendarsScreen(calendarList: List<CalendarRepository.UserCal>, finished: (List<CalendarRepository.UserCal>) -> Unit) {
    val selectedIds = remember { mutableStateListOf<Int>() }
    Column {
        TopAppBar(title = { Text("Select Calendars to use") }, actions = {
            IconButton(onClick = {
                val selectedCalendars = calendarList.filter { selectedIds.contains(it.id) }
                finished(selectedCalendars)
            }) {
                Icon(imageVector = Icons.Default.Check)
            }
        })
        CalendarList(calendarList, selectedIds) {
            selectedIds.toggle(it.id)
        }
    }
}

@Composable
fun CalendarList(calendarList: List<CalendarRepository.UserCal>, selectedIds: List<Int>, selectCalendar: (CalendarRepository.UserCal) -> Unit) {
    LazyColumnFor(items = calendarList) { calendar ->
        CalendarRow(calendar, selectedIds.contains(calendar.id), selectCalendar)
        Divider()
    }
}

@Composable
fun CalendarRow(calendar: CalendarRepository.UserCal, isSelected: Boolean, selectCalendar: (CalendarRepository.UserCal) -> Unit) {
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