package com.radiantmood.calarm.screen.calendars

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
import com.radiantmood.calarm.screen.LoadingState
import com.radiantmood.calarm.util.AppBarAction
import com.radiantmood.calarm.util.LoadingScreen

@Composable
fun CalendarsActivityScreen() {
    val navController = AmbientNavController.current
    if (AmbientPermissionsUtil.current.checkPermissions(navController)) return

    val vm: MainViewModel = AmbientMainViewModel.current
    vm.getCalendarDisplays() // Where should this ACTUALLY be called?

    Column {
        TopAppBar(title = { Text("Select Calendars to use") }, actions = {
            AppBarAction(Icons.Default.Check) {
                navController.popBackStack()
            }
        })
        // TODO: a filter to only show selected calendars
        CalendarScreenContent()
    }
}

@Composable
fun CalendarScreenContent() {
    val vm: MainViewModel = AmbientMainViewModel.current
    val screenModel: CalendarScreenModel by vm.calendarScreen.observeAsState(CalendarScreenModel.getEmpty())

    when(screenModel.state) {
        is LoadingState -> LoadingScreen()
        else -> CalendarList(screenModel.calendarSelectionModels)
    }
}

@Composable
fun CalendarList(calendars: List<CalendarSelectionModel>) {
    // TODO: loading view while waiting?
    LazyColumn {
        items(calendars) { calendar ->
            CalendarRow(calendar)
            Divider()
        }
    }
}

@Composable
fun CalendarRow(calendar: CalendarSelectionModel) {
    Row(
        modifier = Modifier
            .clickable(onClick = calendar.onCalendarToggled)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = calendar.name, Modifier.weight(1f))
        Switch(checked = calendar.isSelected, onCheckedChange = { calendar.onCalendarToggled() })
    }
}