package com.radiantmood.calarm.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.AmbientMainViewModel
import com.radiantmood.calarm.AmbientNavController
import com.radiantmood.calarm.AmbientPermissionsUtil
import com.radiantmood.calarm.MainViewModel
import com.radiantmood.calarm.repo.CalendarRepository.UserCal
import com.radiantmood.calarm.util.AppBarAction

@Composable
fun CalendarsActivityScreen() {
    val navController = AmbientNavController.current
    if (AmbientPermissionsUtil.current.checkPermissions(navController)) return

    val vm: MainViewModel = AmbientMainViewModel.current
    val calendars: List<CalendarDisplay> by vm.calendarDisplays.observeAsState(listOf())
    vm.getCalendarDisplays()

    Column {
        TopAppBar(title = { Text("Select Calendars to use") }, actions = {
            AppBarAction(Icons.Default.Check) {
                navController.popBackStack()
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