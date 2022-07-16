package com.radiantmood.calarm.screen.events

import androidx.compose.ui.graphics.Color
import com.radiantmood.calarm.repo.EventPart
import com.radiantmood.calarm.repo.EventRepository
import com.radiantmood.calarm.repo.UserAlarm
import com.radiantmood.calarm.screen.FinishedUiStateContainer
import java.util.*

data class EventDisplay(val calEvent: EventRepository.CalEvent, val userAlarm: UserAlarm?)

data class CalarmUiState(
    val event: EventUiState,
    val calendar: CalendarUiState,
    val alarms: List<AlarmUiState>,
) {
    val startAlarm: AlarmUiState? get() = alarms.firstOrNull { it.eventPart == EventPart.START }
    val endAlarm: AlarmUiState? get() = alarms.firstOrNull { it.eventPart == EventPart.END }
}

data class EventUiState(
    val name: String,
    val timeRange: String,
    val doesNextEventOverlap: Boolean,
    val debugData: String? = null,
    val onToggleAlarmStart: () -> Unit,
    val onToggleAlarmEnd: () -> Unit,
)

data class AlarmUiState(
    val cal: Calendar,
    val offset: Long,
    val eventPart: EventPart,
    val onIncreaseOffset: () -> Unit,
    val onDecreaseOffset: () -> Unit
)

data class CalendarUiState(val name: String, val color: Color)

data class UnmappedAlarmUiState(val label: String, val onRemoveAlarm: () -> Unit)

sealed class EventsScreenUiState : FinishedUiStateContainer<EventsScreenUiState>() {
    data class Eventful(
        val header: EventfulHeader,
        val eventModels: List<CalarmUiState>,
        val tmoEventModels: List<CalarmUiState>,
        val unmappedAlarms: List<UnmappedAlarmUiState>,
        val showDebugAlarmButton: Boolean
    ) : EventsScreenUiState()

    data class FullscreenMessage(val message: String) : EventsScreenUiState()
}

data class EventfulHeader(val nextAlarmStart: Calendar?, val alarmsLeft: String?)