package com.radiantmood.calarm.screen.events

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radiantmood.calarm.repo.EventPart
import com.radiantmood.calarm.common.getDebugEvent
import com.radiantmood.calarm.common.getFutureCalendar

// TODO: move to settings screen
@Composable
fun DebugAlarmButton() {
    val vm = LocalEventsViewModel.current
    Button({
        val event = getDebugEvent(getFutureCalendar(secondsInFuture = 10))
        vm.scheduleAlarm(event.eventId, event.start, event.title, EventPart.START, 0)
    }) {
        Text("Schedule alarm 20 seconds from now")
    }
}

@Composable
fun UnmappedAlarmRow(unmappedAlarm: UnmappedAlarmUiState) {
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