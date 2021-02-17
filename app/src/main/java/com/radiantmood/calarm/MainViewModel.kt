package com.radiantmood.calarm

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radiantmood.calarm.repo.*
import com.radiantmood.calarm.repo.EventRepository.CalEvent
import com.radiantmood.calarm.screen.FinishedState
import com.radiantmood.calarm.screen.calendars.CalendarScreenModel
import com.radiantmood.calarm.screen.calendars.CalendarSelectionModel
import com.radiantmood.calarm.screen.events.EventDisplay
import com.radiantmood.calarm.screen.events.EventModel
import com.radiantmood.calarm.screen.events.EventsScreenModel
import com.radiantmood.calarm.screen.events.UnmappedAlarmModel
import com.radiantmood.calarm.util.*
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    private var _calendarsScreen = MutableLiveData(CalendarScreenModel.getEmpty())
    val calendarsScreen: LiveData<CalendarScreenModel> = _calendarsScreen

    private var _eventsScreen = MutableLiveData(EventsScreenModel.getEmpty())
    val eventsScreen: LiveData<EventsScreenModel> = _eventsScreen

    private var isDebugMode = false

    private val selectedCalendarsRepo = SelectedCalendarsRepository()
    private val calendarRepo = CalendarRepository()
    private val eventRepo = EventRepository()

    // TODO: group alarm classes into a manager?
    private val alarmRepo = AlarmRepository()
    private val alarmUtil = AlarmUtil()

    fun toggleDebug() {
        isDebugMode = !isDebugMode
        getEventDisplays()
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
            getEventDisplays()
        }
    }

    fun cancelAlarm(alarm: UserAlarm) {
        viewModelScope.launch {
            alarmRepo.remove(alarm.eventId)
            alarmUtil.cancelAlarm(alarm)
            getEventDisplays()
        }
    }

    fun getEventDisplays() = viewModelScope.launch {
        val selectedIds = selectedCalendarsRepo.getAll().toMutableList()
        if (isDebugMode) selectedIds.add(-1) // allow debug calendar
        val events = eventRepo.queryEvents().toMutableList()
        val tmoEvents = eventRepo.queryTomorrowsEvents()
        val eventIds = events.map { it.eventId }
        if (isDebugMode) events.add(0, getDebugEvent()) // add debug event to top
        val eventModels = events.filter { selectedIds.contains(it.calId) }.map { createEventModel(it) }
        val tmoEventModels = tmoEvents.filter { selectedIds.contains(it.calId) }.map { createEventModel(it) }
        val unmappedAlarmModels = alarmRepo.queryAlarms().filter { !eventIds.contains(it.eventId) }.map { createUnmappedAlarmModel(it) }
        val fullScreenMessage = when {
            selectedIds.isEmpty() -> "No calendars selected."
            eventModels.isEmpty() -> "No events today!"
            else -> null
        }
        _eventsScreen.postValue(EventsScreenModel(FinishedState, eventModels, tmoEventModels, unmappedAlarmModels, isDebugMode, fullScreenMessage))
    }

    private fun createUnmappedAlarmModel(it: UserAlarm) =
        UnmappedAlarmModel(
            label = "title: \"${it.title}\"; eventId: \"${it.eventId}\" start: \"${it.calendar.formatTime()}\"",
            onRemoveAlarm = ::cancelAlarm.bind(it)
        )

    private suspend fun createEventModel(event: CalEvent): EventModel {
        val alarm = alarmRepo.getForEvent(event.eventId)
        EventDisplay(event, alarm)
        val timeRange = "${event.start.formatTime()} - ${event.end.formatTime()}"
        val isAlarmSet = alarm != null
        val offsetMillis = (alarm?.calendar?.timeInMillis ?: 0) - event.start.timeInMillis
        val offsetMinutes = TimeUnit.MILLISECONDS.toMinutes(offsetMillis)
        val debugData = if (isDebugMode) "eventId: ${event.eventId}" else null
        return EventModel(
            eventName = event.title,
            timeRange = timeRange,
            isAlarmSet = isAlarmSet,
            alarmOffset = offsetMinutes.toInt(),
            debugData = debugData,
            onToggleAlarm = ::toggleAlarm.bind(event),
            onIncreaseOffset = ::updateAlarmOffset.bind(alarm, 1),
            onDecreaseOffset = ::updateAlarmOffset.bind(alarm, -1)
        )
    }

    fun getCalendarDisplays() = viewModelScope.launch {
        postCalendarUpdate()
    }

    fun toggleSelectedCalendarId(id: Int) = viewModelScope.launch {
        val ids = selectedCalendarsRepo.getAll()
        if (ids.contains(id)) {
            // TODO: should we cancel all related alarms?
            selectedCalendarsRepo.remove(id)
        } else selectedCalendarsRepo.add(id)
        postCalendarUpdate()
    }

    private suspend fun postCalendarUpdate() {
        _calendarsScreen.postValue(CalendarScreenModel(FinishedState, constructDisplays()))
    }

    private suspend fun constructDisplays(): List<CalendarSelectionModel> {
        val selectedIds = selectedCalendarsRepo.getAll()
        return calendarRepo.queryCalendars().map { userCal ->
            CalendarSelectionModel(userCal.name, selectedIds.contains(userCal.id), Color(userCal.colorInt), ::toggleSelectedCalendarId.bind(userCal.id))
        }
    }
}