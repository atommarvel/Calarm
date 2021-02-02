package com.radiantmood.calarm.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.navigate
import com.radiantmood.calarm.AmbientMainViewModel
import com.radiantmood.calarm.AmbientNavController
import com.radiantmood.calarm.AmbientPermissionsUtil
import com.radiantmood.calarm.R
import com.radiantmood.calarm.repo.EventRepository.CalEvent
import com.radiantmood.calarm.repo.UserAlarm
import com.radiantmood.calarm.util.*

@Composable
fun AlarmsScreen() {
    val navController = AmbientNavController.current
    if (AmbientPermissionsUtil.current.checkPermissions(navController)) return

    val vm = AmbientMainViewModel.current
    val events: List<EventDisplay>? by vm.eventDisplays.observeAsState(null)
    vm.getEventDisplays()

    Column {
        // TODO: Strings -> resource ids
        // TODO: Scaffold
        TopAppBar(title = { Text("Today's Alarms") }, actions = {
            // TODO: add a quick way to get to calendar app
            AppBarAction(vectorResource(R.drawable.ic_baseline_calendar_today_24)) {
                navController.navigate("calendars")
            }
        })
        Button({
            val event = getDebugEvent(getFutureCalendar(secondsInFuture = 10))
            vm.scheduleAlarm(event.eventId, event.start, event.title)
        }) {
            Text("Schedule alarm 20 seconds from now")
        }
        val eventList = events // not the same as events since events is backed by a delegate
        if (eventList != null) {
            if (eventList.isEmpty()) {
                NoEventsScreen()
            } else {
                EventsList(eventList = eventList, toggleAlarm = vm::toggleAlarm, vm::setAlarmOffset)
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
fun EventsList(eventList: List<EventDisplay>, toggleAlarm: (CalEvent) -> Unit, setAlarmOffset: (Int, Int) -> Unit) {
    LazyColumn {
        items(eventList) { event ->
            EventRow(event = event, { toggleAlarm(event.calEvent) }, { setAlarmOffset(event.calEvent.eventId, it) })
            Divider()
        }
    }
}

@Composable
fun EventRow(event: EventDisplay, toggleAlarm: () -> Unit, setAlarmOffset: (Int) -> Unit) {
    val offset = event.userAlarm?.offsetMin ?: 0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(event.calEvent.title, fontSize = 18.sp)
            TimeLabel(event)
        }
        if (event.userAlarm != null) {
            OffsetView(
                "Offset: $offset",
                Modifier.weight(1f),
                { setAlarmOffset(offset + 1) },
                { setAlarmOffset(offset - 1) }
            )
        }
        Switch(checked = event.userAlarm != null, onCheckedChange = { toggleAlarm() })
    }
}

@Composable
fun OffsetView(text: String, modifier: Modifier = Modifier, onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text)
        Row {
            AppBarAction(Icons.Default.ArrowDownward, onDecrement)
            AppBarAction(Icons.Default.ArrowUpward, onIncrement)
        }
    }
}

@Composable
fun TimeLabel(event: EventDisplay) {
    val startTime = event.calEvent.start.formatTime()
    val endTime = event.calEvent.end.formatTime()
    Text("$startTime - $endTime")
}

data class EventDisplay(val calEvent: CalEvent, val userAlarm: UserAlarm?)