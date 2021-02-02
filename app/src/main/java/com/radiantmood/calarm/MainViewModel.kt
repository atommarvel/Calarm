package com.radiantmood.calarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radiantmood.calarm.repo.*
import com.radiantmood.calarm.repo.EventRepository.CalEvent
import com.radiantmood.calarm.screen.CalendarDisplay
import com.radiantmood.calarm.screen.EventDisplay
import com.radiantmood.calarm.util.AlarmUtil
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _calendarDisplays = MutableLiveData(listOf<CalendarDisplay>())
    val calendarDisplays: LiveData<List<CalendarDisplay>> = _calendarDisplays

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
        } else scheduleAlarm(event)
    }

    fun scheduleAlarm(event: CalEvent) = viewModelScope.launch {
        val alarm = UserAlarm(event.eventId, event.start)
        alarmRepo.add(alarm)
        alarmUtil.scheduleAlarm(alarm.calendar, alarm.eventId)
        getEventDisplays()
    }

    fun cancelAlarm(alarm: UserAlarm) = viewModelScope.launch {
        alarmRepo.remove(alarm.eventId)
        alarmUtil.cancelAlarm(alarm.eventId)
        getEventDisplays()
    }

    fun getEventDisplays() = viewModelScope.launch {
        val selectedIds = selectedCalendarsRepo.getAll()
        val events = eventRepo.queryEvents()
        val eventDisplays = events.filter { selectedIds.contains(it.calId) }.map {
            val alarm = alarmRepo.getForEvent(it.eventId)
            EventDisplay(it, alarm)
        }.toMutableList()

//        val debugAlarm = alarmRepo.getForEvent(-1)
//        val withDebug = getDebugEventDisplay(debugAlarm)
//        eventDisplays.add(0, withDebug)

        _eventDisplays.postValue(eventDisplays)
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
        _calendarDisplays.postValue(constructDisplays())
    }

    private suspend fun constructDisplays(): List<CalendarDisplay> {
        val selectedIds = selectedCalendarsRepo.getAll()
        return calendarRepo.queryCalendars().map { userCal ->
            CalendarDisplay(userCal, selectedIds.contains(userCal.id))
        }
    }
}