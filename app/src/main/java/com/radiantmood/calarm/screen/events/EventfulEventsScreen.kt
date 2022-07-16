package com.radiantmood.calarm.screen.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.compose.SectionTitle

@Composable
fun EventfulEventsScreen(screenModel: EventsScreenUiState.Eventful) {
    if (screenModel.showDebugAlarmButton) DebugAlarmButton()
    LazyColumn {
        item { EventScreenTopBar() }
        item { EventfulHeader(screenModel.header) }
        EventsList(screenModel.eventModels, screenModel.tmoEventModels, screenModel.unmappedAlarms)
    }
}

fun LazyListScope.EventsList(eventList: List<CalarmUiState>, tmoEventList: List<CalarmUiState>, alarmList: List<UnmappedAlarmUiState>) {
    SectionTitle(eventList.isNotEmpty(), "Today", Modifier.padding(16.dp))
    items(eventList) { model ->
        EventCard(model)
        EventBottomSpacer(model.event.doesNextEventOverlap)
    }
    SectionTitle(tmoEventList.isNotEmpty(), "Tomorrow", Modifier.padding(16.dp))
    items(tmoEventList) { model ->
        EventCard(model)
        EventBottomSpacer(model.event.doesNextEventOverlap)
    }
    SectionTitle(alarmList.isNotEmpty(), "Unmapped Alarms", Modifier.padding(16.dp))
    items(alarmList) { unmappedAlarm ->
        UnmappedAlarmRow(unmappedAlarm)
    }
}

@Composable
fun EventBottomSpacer(doesNextEventOverlap: Boolean) {
    if (doesNextEventOverlap) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .height(18.dp)
                    .width(1.dp)
                    .background(color = Color.White.copy(alpha = 0.38f))
                    .align(Alignment.Center)
            )
        }
    } else {
        Spacer(modifier = Modifier.height(18.dp))
    }
}