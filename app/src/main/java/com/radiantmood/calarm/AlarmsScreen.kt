package com.radiantmood.calarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.radiantmood.calarm.CalendarRepository.CalEvent


const val TAG = "araiff"

@Composable
fun AlarmsScreen(navController: NavController) {
    Permissions.current.checkPermission(navController)
    val vm: MainViewModel = viewModel()
    val events: List<EventDisplay> by vm.eventDisplays.observeAsState(listOf())
    vm.getEventDisplays()

    Column {
        TopAppBar(title = { Text("Today's Alarms") }, actions = {
            AppBarAction(vectorResource(R.drawable.ic_baseline_calendar_today_24)) {
                navController.navigate("calendars")
            }
        })
        LazyColumn {
            items(events) { event ->
                Text(event.calEvent.title)
            }
        }
        // TODO: Loading
    }
}

data class EventDisplay(val calEvent: CalEvent)