package com.radiantmood.calarm.screen.events

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radiantmood.calarm.repo.AlarmRepository
import com.radiantmood.calarm.repo.EventRepository
import com.radiantmood.calarm.repo.EventRepository.CalEvent
import com.radiantmood.calarm.repo.SelectedCalendarsRepository
import com.radiantmood.calarm.repo.UserAlarm
import com.radiantmood.calarm.screen.LoadingModelContainer
import com.radiantmood.calarm.screen.ModelContainer
import com.radiantmood.calarm.util.*
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class EventsViewModel : ViewModel() {
    private var _eventsScreen = MutableLiveData<ModelContainer<EventsScreenModel>>(LoadingModelContainer())
    val eventsScreen: LiveData<ModelContainer<EventsScreenModel>> = _eventsScreen

    private var isDebugMode = false

    private val selectedCalendarsRepo = SelectedCalendarsRepository()
    private val eventRepo = EventRepository()

    // TODO: group alarm classes into a manager?
    private val alarmRepo = AlarmRepository()
    private val alarmUtil = AlarmUtil()

    fun toggleDebug() {
        isDebugMode = !isDebugMode
        getData()
    }

    fun toggleAlarm(event: CalEvent) = viewModelScope.launch {
        val alarm = alarmRepo.getForEvent(event.eventId)
        if (alarm != null) {
            cancelAlarm(alarm)
        } else scheduleAlarm(event.eventId, event.start, event.title)
    }

    fun updateAlarmOffset(alarm: UserAlarm?, offsetChangeMinutes: Int) = viewModelScope.launch {
        if (alarm != null) {
            alarmUtil.cancelAlarm(alarm)
            val calendar = CalendarAtTime(alarm.calendar.timeInMillis + TimeUnit.MINUTES.toMillis(offsetChangeMinutes.toLong()))
            scheduleAlarm(alarm.eventId, calendar, alarm.title)
        }
    }

    fun scheduleAlarm(eventId: Int, start: Calendar, title: String) {
        viewModelScope.launch {
            val alarm = UserAlarm(eventId, start, title)
            alarmRepo.add(alarm)
            alarmUtil.scheduleAlarm(alarm)
            getData()
        }
    }

    fun cancelAlarm(alarm: UserAlarm) {
        viewModelScope.launch {
            alarmRepo.remove(alarm.eventId)
            alarmUtil.cancelAlarm(alarm)
            getData()
        }
    }

    private class HeaderBuilder(var firstAlarm: Calendar? = null, var alarmCount: Int = 0) {
        /**
         * Consumption assumes that the event models are given by order of start time!
         */
        fun consumeAlarm(alarm: UserAlarm?) {
            alarm?.let {
                alarmCount++
                if (firstAlarm == null) {
                    firstAlarm = alarm.calendar
                }
            }
        }

        fun produceHeader() = EventfulHeader(firstAlarm, "$alarmCount Calarms left today")
    }

    /**
     * TODO: PTR
     */
    fun getData() = viewModelScope.launch {
        val headerBuilder = HeaderBuilder()
        val selectedIds = selectedCalendarsRepo.getAll().toMutableList()
        val events = eventRepo.queryEvents(selectedIds).toMutableList()
        val tmoEvents = eventRepo.queryTomorrowsEvents(selectedIds)
        if (isDebugMode) selectedIds.add(-1) // allow debug calendar
        val eventIds = events.map { it.eventId }.toMutableList().apply {
            tmoEvents.forEach { add(it.eventId) }
        }.toList()
        if (isDebugMode) events.add(0, getDebugEvent()) // add debug event to top
        val processedEventIds = mutableSetOf<Int>()
        val eventModels = events.mapIndexed { index, event ->
            val previouslyProcessed = processedEventIds.contains(event.eventId)
            processedEventIds.add(event.eventId)
            createEventModel(event, events.getOrNull(index + 1), previouslyProcessed, headerBuilder)
        }
        val tmoEventModels = tmoEvents.mapIndexed { index, event ->
            val previouslyProcessed = processedEventIds.contains(event.eventId)
            processedEventIds.add(event.eventId)
            createEventModel(event, tmoEvents.getOrNull(index + 1), previouslyProcessed, headerBuilder)
        }
        val unmappedAlarmModels = alarmRepo.queryAlarms()
            .filter { !eventIds.contains(it.eventId) }
            .map { createUnmappedAlarmModel(it) }
        val fullScreenMessage = when {
            selectedIds.isEmpty() -> "No calendars selected."
            eventModels.isEmpty() && tmoEventModels.isEmpty() -> "No events today!"
            else -> null
        }
        val model = fullScreenMessage?.let { EventsScreenModel.FullscreenMessage(it) } ?: EventsScreenModel.Eventful(
            headerBuilder.produceHeader(),
            eventModels,
            tmoEventModels,
            unmappedAlarmModels,
            isDebugMode
        )
        _eventsScreen.postValue(model)
    }

    private fun createUnmappedAlarmModel(it: UserAlarm) =
        UnmappedAlarmModel(
            label = "title: \"${it.title}\"; eventId: \"${it.eventId}\" start: \"${it.calendar.formatTime()}\"",
            onRemoveAlarm = ::cancelAlarm.bind(it)
        )

    private suspend fun createEventModel(
        event: CalEvent,
        nextEvent: CalEvent? = null,
        previouslyProcessed: Boolean = false,
        headerBuilder: HeaderBuilder? = null
    ): EventModel {
        val alarm =
            if (!previouslyProcessed) alarmRepo.getForEvent(event.eventId) else null // TODO: reject alarm if it is 24 hr off (aka it's a daily recurring event)
        headerBuilder?.consumeAlarm(alarm)
        EventDisplay(event, alarm)
        val timeRange = "${event.start.formatTime()} - ${event.end.formatTime()}"
        val isAlarmSet = alarm != null
        val offsetMillis = (alarm?.calendar?.timeInMillis ?: 0) - event.start.timeInMillis
        val offsetMinutes = TimeUnit.MILLISECONDS.toMinutes(offsetMillis)
        val debugData = if (isDebugMode) "eventId: ${event.eventId}" else null
        val doesNextEventOverlap = if (nextEvent != null) {
            event.end.after(nextEvent.start)
        } else false
        return EventModel(
            eventName = event.title,
            timeRange = timeRange,
            isAlarmSet = isAlarmSet,
            alarmOffset = offsetMinutes.toInt(),
            calColor = Color(event.calColorInt),
            calName = "Schedule", // TODO: get actual name
            doesNextEventOverlap = doesNextEventOverlap,
            debugData = debugData,
            onToggleAlarm = ::toggleAlarm.bind(event),
            onIncreaseOffset = ::updateAlarmOffset.bind(alarm, 1),
            onDecreaseOffset = ::updateAlarmOffset.bind(alarm, -1)
        )
    }
}