package com.radiantmood.calarm.screen.events


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
import com.radiantmood.calarm.screen.LoadingState
import com.radiantmood.calarm.util.*

@Composable
fun EventsScreen() {
    val navController = AmbientNavController.current
    if (AmbientPermissionsUtil.current.checkPermissions(navController)) return

    val vm = AmbientMainViewModel.current
    val screenModel: EventsScreenModel by vm.eventsScreen.observeAsState(EventsScreenModel.getEmpty())
    vm.getEventDisplays()

    Column {
        // TODO: Strings -> resource ids
        // TODO: Scaffold
        TopAppBar(title = { Text("Today's Alarms") }, actions = {
            // TODO: add a quick way to get to calendar app
            // TODO: add a quick way to create an invisible event for 11:59 pm for testing
            // TODO: enable/disable debug calendar event
            AppBarAction(vectorResource(R.drawable.ic_baseline_calendar_today_24)) {
                navController.navigate("calendars")
            }
        })
        DebugAlarmButton()
        EventsContent(screenModel)
    }
}

@Composable
fun DebugAlarmButton() {
    val vm = AmbientMainViewModel.current
    Button({
        val event = getDebugEvent(getFutureCalendar(secondsInFuture = 10))
        vm.scheduleAlarm(event.eventId, event.start, event.title)
    }) {
        Text("Schedule alarm 20 seconds from now")
    }
}

@Composable
fun EventsContent(screenModel: EventsScreenModel) {
    when {
        screenModel.state is LoadingState -> LoadingScreen()
        screenModel.eventModels.isEmpty() -> NoEventsScreen()
        else -> EventsList(screenModel.eventModels)
    }
}

@Composable
fun NoEventsScreen() = Fullscreen {
    Text("No more events today!")
}

@Composable
fun EventsList(eventList: List<EventModel>) {
    LazyColumn {
        items(eventList) { event ->
            EventRow(event)
            Divider()
        }
    }
}

@Composable
fun EventRow(event: EventModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(event.eventName, fontSize = 18.sp)
            Text(event.timeRange)
        }
        if (event.isAlarmSet) {
            OffsetView(Modifier.weight(1f), event)
        }
        Switch(checked = event.isAlarmSet, onCheckedChange = { event.onToggleAlarm() })
    }
}

@Composable
fun OffsetView(modifier: Modifier = Modifier, event: EventModel) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Offset: ${event.alarmOffset}")
        Row {
            AppBarAction(Icons.Default.ArrowDownward, event.onDecreaseOffset)
            AppBarAction(Icons.Default.ArrowUpward, event.onIncreaseOffset)
        }
    }
}