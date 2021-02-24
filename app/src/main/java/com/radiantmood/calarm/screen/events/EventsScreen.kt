package com.radiantmood.calarm.screen.events


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.navigate
import com.radiantmood.calarm.BuildConfig
import com.radiantmood.calarm.LocalAppBarTitle
import com.radiantmood.calarm.LocalNavController
import com.radiantmood.calarm.LocalPermissionsUtil
import com.radiantmood.calarm.compose.*
import com.radiantmood.calarm.screen.LoadingState
import com.radiantmood.calarm.util.getDebugEvent
import com.radiantmood.calarm.util.getFutureCalendar

val LocalEventsViewModel = compositionLocalOf<EventsViewModel> { error("No EventsViewModel") }

@Composable
fun EventsActivityScreen() {
    val navController = LocalNavController.current
    if (LocalPermissionsUtil.current.checkPermissions(navController)) return
    val vm: EventsViewModel = viewModel()
    vm.getData()
    Providers(
        LocalAppBarTitle provides "Events",
        LocalEventsViewModel provides vm
    ) {
        EventsScreen()
    }
}

@Composable
fun EventsScreen() {
    val navController = LocalNavController.current
    val vm = LocalEventsViewModel.current
    val screenModel: EventsScreenModel by vm.eventsScreen.observeAsState(EventsScreenModel.getEmpty())

    Column {
        // TODO: Strings -> resource ids
        // TODO: Scaffold
        CalarmTopAppBar(actions = {
            // TODO: add a quick way to get to calendar app
            // TODO: add a quick way to create an invisible event for 11:59 pm for testing
            if (BuildConfig.DEBUG) {
                AppBarAction(imageVector = Icons.Default.BugReport) {
                    // TODO: toggling debug and then flinging the list results in a compose embedded crash. Why?
                    vm.toggleDebug()
                }
            }
            // TODO: vectorResource method deprecated!
            AppBarAction(Icons.Default.Settings) {
                navController.navigate("settings")
            }
        })
        if (screenModel.showDebugAlarmButton) DebugAlarmButton()
        when {
            screenModel.state is LoadingState -> LoadingScreen()
            !screenModel.fullScreenMessage.isNullOrBlank() -> MessageScreen(screenModel.fullScreenMessage!!)
            else -> EventsList(screenModel.eventModels, screenModel.tmoEventModels, screenModel.unmappedAlarms)
        }
    }
}

@Composable
fun DebugAlarmButton() {
    val vm = LocalEventsViewModel.current
    Button({
        val event = getDebugEvent(getFutureCalendar(secondsInFuture = 10))
        vm.scheduleAlarm(event.eventId, event.start, event.title)
    }) {
        Text("Schedule alarm 20 seconds from now")
    }
}

@Composable
fun MessageScreen(message: String) = Fullscreen {
    Text(message)
}

@Composable
fun EventsList(eventList: List<EventModel>, tmoEventList: List<EventModel>, alarmList: List<UnmappedAlarmModel>) {
    LazyColumn {
        SectionTitle(eventList.isNotEmpty(), "Today's Events")
        items(eventList) { event ->
            EventRow(event)
            if (!event.doesNextEventOverlap) {
                Divider()
            }
        }
        SectionTitle(tmoEventList.isNotEmpty(), "Tomorrow's Events")
        items(tmoEventList) { event ->
            EventRow(event)
            if (!event.doesNextEventOverlap) {
                Divider()
            }
        }
        SectionTitle(alarmList.isNotEmpty(), "Unmapped Alarms")
        items(alarmList) { unmappedAlarm ->
            UnmappedAlarmRow(unmappedAlarm)
        }
    }
}

@Composable
fun UnmappedAlarmRow(unmappedAlarm: UnmappedAlarmModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(unmappedAlarm.label, modifier = Modifier.weight(1f))
        Switch(checked = true, onCheckedChange = {
            unmappedAlarm.onRemoveAlarm()
        })
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
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(event.calColor, CircleShape)
                        .size(12.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(event.eventName, fontSize = 18.sp)
                    Text(event.timeRange)
                }
                if (event.isAlarmSet) {
                    OffsetView(Modifier.weight(1f), event)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { event.onToggleAlarm() }) {
                        val img = if (event.isAlarmSet) Icons.Default.AlarmOn else Icons.Default.AlarmOff
                        val tint = if (event.isAlarmSet) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground.copy(alpha = .3f)
                        // TODO: package tint and imagevector and onClick into a CalarmIconButton model and deliver from the viewmodel
                        Icon(img, null, tint = tint) // TODO: null
                    }
                }
            }
            event.debugData?.let {
                Text(it)
            }
        }
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