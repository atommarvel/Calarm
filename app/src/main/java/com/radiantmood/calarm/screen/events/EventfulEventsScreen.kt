package com.radiantmood.calarm.screen.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.compose.SectionTitle

fun LazyListScope.EventsList(eventList: List<CalarmModel>, tmoEventList: List<CalarmModel>, alarmList: List<UnmappedAlarmModel>) {
    SectionTitle(eventList.isNotEmpty(), "Today", Modifier.padding(16.dp))
    items(eventList) { model ->
        EventRow(model)
        EventBottomSpacer(model.event.doesNextEventOverlap)
    }
    SectionTitle(tmoEventList.isNotEmpty(), "Tomorrow", Modifier.padding(16.dp))
    items(tmoEventList) { model ->
        EventRow(model)
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
        Box(
            modifier = Modifier
                .height(18.dp)
                .width(1.dp)
                .background(color = Color.Black.copy(alpha = 0.38f))
        )
    } else {
        Spacer(modifier = Modifier.height(18.dp))
    }
}