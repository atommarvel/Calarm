package com.radiantmood.calarm.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.navigate
import com.radiantmood.calarm.AmbientMainViewModel
import com.radiantmood.calarm.AmbientNavController
import com.radiantmood.calarm.AmbientPermissionsUtil
import com.radiantmood.calarm.R
import com.radiantmood.calarm.repo.EventRepository.CalEvent
import com.radiantmood.calarm.repo.UserAlarm
import com.radiantmood.calarm.util.AppBarAction
import com.radiantmood.calarm.util.Fullscreen
import com.radiantmood.calarm.util.LoadingScreen
import com.radiantmood.calarm.util.formatTime

@Composable
fun AlarmsScreen() {
    val navController = AmbientNavController.current
    if (AmbientPermissionsUtil.current.checkPermissions(navController)) return

    val vm = AmbientMainViewModel.current
    val events: List<EventDisplay>? by vm.eventDisplays.observeAsState(null)
    vm.getEventDisplays()

    Column {
        TopAppBar(title = { Text("Today's Alarms") }, actions = {
            // TODO: add a quick way to get to calendar app
            AppBarAction(vectorResource(R.drawable.ic_baseline_calendar_today_24)) {
                navController.navigate("calendars")
            }
        })
        val eventList = events // not the same as events since events is backed by a delegate
        if (eventList != null) {
            if (eventList.isEmpty()) {
                NoEventsScreen()
            } else {
                EventsList(eventList = eventList, toggleAlarm = vm::toggleAlarm)
            }
        } else {
            LoadingScreen()
        }
    }
}

@Composable
fun NoEventsScreen() = Fullscreen {
    Text("No more events today!")
}

@Composable
fun EventsList(eventList: List<EventDisplay>, toggleAlarm: (CalEvent) -> Unit) {
    LazyColumn {
        items(eventList) { event ->
            EventRow(event = event, toggleAlarm = toggleAlarm)
        }
    }
}

@Composable
fun EventRow(event: EventDisplay, toggleAlarm: (CalEvent) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { toggleAlarm(event.calEvent) })
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        EventLabel(event, Modifier.weight(1f))
        Switch(checked = event.userAlarm != null, onCheckedChange = { toggleAlarm(event.calEvent) })
    }
}

@Composable
fun EventLabel(event: EventDisplay, modifier: Modifier) {
    val startTime = event.calEvent.start.formatTime()
    val endTime = event.calEvent.end.formatTime()
    Text("\"${event.calEvent.title}\": $startTime - $endTime", modifier = modifier)
}

data class EventDisplay(val calEvent: CalEvent, val userAlarm: UserAlarm?)