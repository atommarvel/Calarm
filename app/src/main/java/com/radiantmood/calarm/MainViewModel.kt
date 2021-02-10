package com.radiantmood.calarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radiantmood.calarm.repo.*
import com.radiantmood.calarm.repo.EventRepository.CalEvent
import com.radiantmood.calarm.screen.EventDisplay
import com.radiantmood.calarm.screen.FinishedState
import com.radiantmood.calarm.screen.calendars.CalendarScreenModel
import com.radiantmood.calarm.screen.calendars.CalendarSelectionModel
import com.radiantmood.calarm.util.AlarmUtil
import com.radiantmood.calarm.util.bind
import com.radiantmood.calarm.util.getDebugEventDisplay
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {
    private var _calendarScreen = MutableLiveData(CalendarScreenModel.getEmpty())
    val calendarScreen: LiveData<CalendarScreenModel> = _calendarScreen

    private var _eventDisplays = MutableLiveData(listOf<EventDisplay>())
    val eventDisplays: LiveData<List<EventDisplay>> = _eventDisplays

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

    fun setAlarmOffset(eventId: Int, offset: Int) = viewModelScope.launch {
        val alarm = alarmRepo.getForEvent(eventId)
        if (alarm != null) {
            alarmUtil.cancelAlarm(alarm)
            scheduleAlarm(alarm.eventId, alarm.calendar, alarm.title, offset)
        }
    }

    fun scheduleAlarm(eventId: Int, start: Calendar, title: String, offset: Int = 0) = viewModelScope.launch {
        val alarm = UserAlarm(eventId, start, title, offset)
        alarmRepo.add(alarm)
        alarmUtil.scheduleAlarm(alarm)
        getEventDisplays()
    }

    fun cancelAlarm(alarm: UserAlarm) = viewModelScope.launch {
        alarmRepo.remove(alarm.eventId)
        alarmUtil.cancelAlarm(alarm)
        getEventDisplays()
    }

    fun getEventDisplays() = viewModelScope.launch {
        val selectedIds = selectedCalendarsRepo.getAll()
        val events = eventRepo.queryEvents()
        val models = events.filter { selectedIds.contains(it.calId) }.map {
            val alarm = alarmRepo.getForEvent(it.eventId)
            EventDisplay(it, alarm)
        }.toMutableList()

        val debugAlarm = alarmRepo.getForEvent(-1)
        val withDebug = getDebugEventDisplay(debugAlarm)
        models.add(0, withDebug)

        _eventDisplays.postValue(models)
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
        _calendarScreen.postValue(CalendarScreenModel(FinishedState, constructDisplays()))
    }

    private suspend fun constructDisplays(): List<CalendarSelectionModel> {
        val selectedIds = selectedCalendarsRepo.getAll() // TODO: subscribe to changes to this instead
        return calendarRepo.queryCalendars().map { userCal ->
            CalendarSelectionModel(userCal.name, selectedIds.contains(userCal.id), ::toggleSelectedCalendarId.bind(userCal.id))
        }
    }
}