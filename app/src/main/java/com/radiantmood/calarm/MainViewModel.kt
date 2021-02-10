package com.radiantmood.calarm

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
import com.radiantmood.calarm.util.*
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    private var _calendarsScreen = MutableLiveData(CalendarScreenModel.getEmpty())
    val calendarsScreen: LiveData<CalendarScreenModel> = _calendarsScreen

    private var _eventsScreen = MutableLiveData(EventsScreenModel.getEmpty())
    val eventsScreen: LiveData<EventsScreenModel> = _eventsScreen

    private val selectedCalendarsRepo = SelectedCalendarsRepository()
    private val calendarRepo = CalendarRepository()
    private val eventRepo = EventRepository()

    // TODO: group alarm classes into a manager
    private val alarmRepo = AlarmRepository()
    private val alarmUtil = AlarmUtil()

    fun toggleAlarm(event: CalEvent) = viewModelScope.launch {
        val alarm = alarmRepo.getForEvent(event.eventId)
        if (alarm != null) {
            cancelAlarm(alarm)
        } else scheduleAlarm(event.eventId, event.start, event.title)
    }

    fun updateAlarmOffset(alarm: UserAlarm?, offsetChangeMinutes: Int) = viewModelScope.launch {
        if (alarm != null) {
            alarmUtil.cancelAlarm(alarm)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = alarm.calendar.timeInMillis + TimeUnit.SECONDS.toMillis(offsetChangeMinutes.toLong())
            }
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
        selectedIds.add(-1) // allow debug calendar
        val events = eventRepo.queryEvents().toMutableList()
        events.add(0, getDebugEvent()) // add debug event to top
        val models = events.filter { selectedIds.contains(it.calId) }.map { event ->
            val alarm = alarmRepo.getForEvent(event.eventId)
            EventDisplay(event, alarm)
            val timeRange = "${event.start.formatTime()} - ${event.end.formatTime()}"
            val isAlarmSet = alarm != null
            val offsetMillis = (alarm?.calendar?.timeInMillis ?: 0) - event.start.timeInMillis
            val offsetSeconds = TimeUnit.MILLISECONDS.toSeconds(offsetMillis)
            EventModel(
                event.title,
                timeRange,
                isAlarmSet,
                offsetSeconds.toInt(),
                this@MainViewModel::toggleAlarm.bind(event),
                this@MainViewModel::updateAlarmOffset.bind(alarm, 1),
                this@MainViewModel::updateAlarmOffset.bind(alarm, 2)
            )
        }.toMutableList()

        _eventsScreen.postValue(EventsScreenModel(FinishedState, models))
    }

    fun getCalendarDisplays() = viewModelScope.launch {
        postCalendarUpdate()
    }

    fun toggleSelectedCalendarId(id: Int) = viewModelScope.launch {
        val ids = selectedCalendarsRepo.getAll()
        if (ids.contains(id)) {
            selectedCalendarsRepo.remove(id)
        } else selectedCalendarsRepo.add(id)
        postCalendarUpdate()
    }

    private suspend fun postCalendarUpdate() {
        _calendarsScreen.postValue(CalendarScreenModel(FinishedState, constructDisplays()))
    }

    private suspend fun constructDisplays(): List<CalendarSelectionModel> {
        val selectedIds = selectedCalendarsRepo.getAll() // TODO: subscribe to changes to this instead
        return calendarRepo.queryCalendars().map { userCal ->
            CalendarSelectionModel(userCal.name, selectedIds.contains(userCal.id), ::toggleSelectedCalendarId.bind(userCal.id))
        }
    }
}