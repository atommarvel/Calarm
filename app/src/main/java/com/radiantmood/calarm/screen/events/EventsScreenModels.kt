package com.radiantmood.calarm.screen.events

import androidx.compose.ui.graphics.Color
import com.radiantmood.calarm.repo.EventRepository
import com.radiantmood.calarm.repo.UserAlarm
import com.radiantmood.calarm.screen.FinishedModelContainer
import java.util.*

data class EventDisplay(val calEvent: EventRepository.CalEvent, val userAlarm: UserAlarm?)

data class EventModel(
    val eventName: String,
    val timeRange: String,
    val isAlarmSet: Boolean,
    val alarmOffset: Int,
    val calColor: Color,
    val calName: String,
    val doesNextEventOverlap: Boolean,
    val debugData: String? = null,
    val onToggleAlarm: () -> Unit,
    val onIncreaseOffset: () -> Unit,
    val onDecreaseOffset: () -> Unit
)

data class UnmappedAlarmModel(val label: String, val onRemoveAlarm: () -> Unit)

sealed class EventsScreenModel : FinishedModelContainer<EventsScreenModel>() {
    data class Eventful(
        val header: EventfulHeader,
        val eventModels: List<EventModel>,
        val tmoEventModels: List<EventModel>,
        val unmappedAlarms: List<UnmappedAlarmModel>,
        val showDebugAlarmButton: Boolean
    ) : EventsScreenModel()

    data class FullscreenMessage(val message: String) : EventsScreenModel()
}

data class EventfulHeader(val nextAlarmStart: Calendar?, val alarmsLeft: String?)