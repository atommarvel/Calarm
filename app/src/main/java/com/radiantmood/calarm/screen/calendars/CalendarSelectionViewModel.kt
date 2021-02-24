package com.radiantmood.calarm.screen.calendars

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radiantmood.calarm.repo.CalendarRepository
import com.radiantmood.calarm.repo.SelectedCalendarsRepository
import com.radiantmood.calarm.screen.FinishedState
import com.radiantmood.calarm.util.bind
import kotlinx.coroutines.launch

class CalendarSelectionViewModel : ViewModel() {
    private var _calendarsScreen = MutableLiveData(CalendarsSelectionScreenModel.getEmpty())
    val calendarsScreen: LiveData<CalendarsSelectionScreenModel> = _calendarsScreen

    private val selectedCalendarsRepo = SelectedCalendarsRepository()
    private val calendarRepo = CalendarRepository()

    private suspend fun postCalendarUpdate() {
        _calendarsScreen.postValue(CalendarsSelectionScreenModel(FinishedState, constructDisplays()))
    }

    private suspend fun constructDisplays(): List<CalendarSelectionModel> {
        val selectedIds = selectedCalendarsRepo.getAll()
        return calendarRepo.queryCalendars().map { userCal ->
            CalendarSelectionModel(userCal.name, selectedIds.contains(userCal.id), Color(userCal.colorInt), ::toggleSelectedCalendarId.bind(userCal.id))
        }
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
}