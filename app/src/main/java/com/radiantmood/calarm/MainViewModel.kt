package com.radiantmood.calarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// TODO: move up to being created at App() level
class MainViewModel : ViewModel() {
    private var _calendarDisplays = MutableLiveData(listOf<CalendarDisplay>())
    val calendarDisplays: LiveData<List<CalendarDisplay>> = _calendarDisplays

    private var _eventDisplays = MutableLiveData(listOf<EventDisplay>())
    val eventDisplays: LiveData<List<EventDisplay>> = _eventDisplays

    private val selectedCalendarsRepo = SelectedCalendarsRepository()
    private val calendarRepo = CalendarRepository()

    fun getEventDisplays() = viewModelScope.launch {
        val selectedIds = selectedCalendarsRepo.getAll()
        val events = calendarRepo.queryEvents()
        val eventDisplays = events.filter { selectedIds.contains(it.calId) }.map { EventDisplay(it) }
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