package com.radiantmood.calarm.screen.events

import androidx.compose.ui.graphics.Color
import com.radiantmood.calarm.repo.EventRepository
import com.radiantmood.calarm.repo.UserAlarm
import com.radiantmood.calarm.screen.FinishedModelContainer
import java.util.*

data class EventDisplay(val calEvent: EventRepository.CalEvent, val userAlarm: UserAlarm?)

data class CalarmModel(
    val event: EventModel,
    val calendar: CalendarModel,
    val alarm: AlarmModel?,
)

data class EventModel(
    val name: String,
    val timeRange: String,
    val doesNextEventOverlap: Boolean,
    val debugData: String? = null,
    val onToggleAlarm: () -> Unit,
)

data class AlarmModel(
    val cal: Calendar,
    val offset: Long,
    val onIncreaseOffset: () -> Unit,
    val onDecreaseOffset: () -> Unit
)

data class CalendarModel(val name: String, val color: Color)

data class UnmappedAlarmModel(val label: String, val onRemoveAlarm: () -> Unit)

sealed class EventsScreenModel : FinishedModelContainer<EventsScreenModel>() {
    data class Eventful(
        val header: EventfulHeader,
        val eventModels: List<CalarmModel>,
        val tmoEventModels: List<CalarmModel>,
        val unmappedAlarms: List<UnmappedAlarmModel>,
        val showDebugAlarmButton: Boolean
    ) : EventsScreenModel()

    data class FullscreenMessage(val message: String) : EventsScreenModel()
}

data class EventfulHeader(val nextAlarmStart: Calendar?, val alarmsLeft: String?)