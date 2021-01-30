package com.radiantmood.calarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private var _calendarDisplays = MutableLiveData(listOf<CalendarDisplay>())
    val calendarDisplays: LiveData<List<CalendarDisplay>> = _calendarDisplays

    private val selectedCalendarsRepo = SelectedCalendarsRepository()
    private val calendarRepo = CalendarRepository()

    fun getCalendarDisplays() = viewModelScope.launch {
        postUpdate()
    }

    fun toggleSelectedCalendarId(id: Int) = viewModelScope.launch {
        val ids = selectedCalendarsRepo.getAll()
        if (ids.contains(id)) {
            selectedCalendarsRepo.remove(id)
        } else selectedCalendarsRepo.add(id)
        postUpdate()
    }

    private suspend fun postUpdate() {
        _calendarDisplays.postValue(constructDisplays())
    }

    private suspend fun constructDisplays(): List<CalendarDisplay> {
        val selectedIds = selectedCalendarsRepo.getAll()
        return calendarRepo.queryCalendars().map { userCal ->
            CalendarDisplay(userCal, selectedIds.contains(userCal.id))
        }
    }
}