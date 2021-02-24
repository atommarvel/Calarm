package com.radiantmood.calarm.screen.events

import androidx.compose.ui.graphics.Color
import com.radiantmood.calarm.repo.EventRepository
import com.radiantmood.calarm.repo.UserAlarm
import com.radiantmood.calarm.screen.LoadingState
import com.radiantmood.calarm.screen.ModelState

data class EventDisplay(val calEvent: EventRepository.CalEvent, val userAlarm: UserAlarm?)

data class EventModel(
    val eventName: String,
    val timeRange: String,
    val isAlarmSet: Boolean,
    val alarmOffset: Int,
    val calColor: Color,
    val doesNextEventOverlap: Boolean,
    val debugData: String? = null,
    val onToggleAlarm: () -> Unit,
    val onIncreaseOffset: () -> Unit,
    val onDecreaseOffset: () -> Unit
)

data class UnmappedAlarmModel(val label: String, val onRemoveAlarm: () -> Unit)

data class EventsScreenModel(
    val state: ModelState,
    val eventModels: List<EventModel>,
    val tmoEventModels: List<EventModel>,
    val unmappedAlarms: List<UnmappedAlarmModel>,
    val showDebugAlarmButton: Boolean,
    val fullScreenMessage: String? = null,
) {
    companion object {
        fun getEmpty() = EventsScreenModel(LoadingState, emptyList(), emptyList(), emptyList(), false)
    }
}