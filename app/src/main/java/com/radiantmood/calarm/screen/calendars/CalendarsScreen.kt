package com.radiantmood.calarm.screen.calendars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.LocalMainViewModel
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.LocalPermissionsUtil
import com.radiantmood.calarm.screen.LoadingState
import com.radiantmood.calarm.util.AppBarAction
import com.radiantmood.calarm.util.LoadingScreen

@Composable
fun CalendarsActivityScreen() {
    val navController = LocalNavController.current
    if (LocalPermissionsUtil.current.checkPermissions(navController)) return
    LocalMainViewModel.current.getCalendarDisplays()
    CalendarsScreen()
}

@Composable
fun CalendarsScreen() {
    val navController = LocalNavController.current
    val screenModel: CalendarScreenModel by LocalMainViewModel.current.calendarsScreen.observeAsState(CalendarScreenModel.getEmpty())
    Column {
        TopAppBar(title = { Text("Select Calendars to use") }, actions = {
            AppBarAction(Icons.Default.Check) {
                navController.popBackStack()
            }
        })
        // TODO: a filter to only show selected calendars
        CalendarScreenContent(screenModel)
    }
}

@Composable
fun CalendarScreenContent(screenModel: CalendarScreenModel) {
    when (screenModel.state) {
        is LoadingState -> LoadingScreen()
        else -> CalendarList(screenModel.calendarSelectionModels)
    }
}

@Composable
fun CalendarList(calendars: List<CalendarSelectionModel>) {
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
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(calendar.color, CircleShape)
                .size(12.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(text = calendar.name, Modifier.weight(1f))
        Switch(checked = calendar.isSelected, onCheckedChange = { calendar.onCalendarToggled() })
    }
}