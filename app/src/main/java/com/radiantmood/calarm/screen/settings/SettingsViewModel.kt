package com.radiantmood.calarm.screen.settings

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radiantmood.calarm.repo.CalendarRepository
import com.radiantmood.calarm.repo.DailyNotifRepo
import com.radiantmood.calarm.repo.SelectedCalendarsRepository
import com.radiantmood.calarm.screen.LoadingModelContainer
import com.radiantmood.calarm.screen.ModelContainer
import com.radiantmood.calarm.screen.calendars.CalendarSelectionModel
import com.radiantmood.calarm.util.bind
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private var _settingsScreen = MutableLiveData<ModelContainer<SettingsScreenModel>>(LoadingModelContainer())
    val settingsScreen: LiveData<ModelContainer<SettingsScreenModel>> = _settingsScreen

    private val selectedCalendarsRepo = SelectedCalendarsRepository()
    private val calendarRepo = CalendarRepository()

    fun getData() = viewModelScope.launch {
        postUpdate()
    }

    private suspend fun postUpdate() {
        _settingsScreen.postValue(
            SettingsScreenModel(
                selectedCalendars = getSelectedCalendars(),
                dailyNotifTime = DailyNotifRepo.getDailyNotifTime()
            )
        )
    }

    private suspend fun getSelectedCalendars(): List<CalendarSelectionModel> {
        val selectedIds = selectedCalendarsRepo.getAll()
        return calendarRepo.queryCalendars()
            .filter { selectedIds.contains(it.id) }
            .map { userCal ->
                CalendarSelectionModel(userCal.name, selectedIds.contains(userCal.id), Color(userCal.colorInt), ::toggleSelectedCalendarId.bind(userCal.id))
            }
    }

    fun toggleSelectedCalendarId(id: Int) = viewModelScope.launch {
        val ids = selectedCalendarsRepo.getAll()
        if (ids.contains(id)) {
            // TODO: should we cancel all related alarms?
            selectedCalendarsRepo.remove(id)
        } else selectedCalendarsRepo.add(id)
        postUpdate()
    }

    fun toggleDailyNotifs() = viewModelScope.launch {
        DailyNotifRepo.toggleIsDailyNotifEnabled()
        postUpdate()
    }

    fun setDailyNotificationHour(hour: Int, minute: Int) = viewModelScope.launch {
        DailyNotifRepo.setDailyNotifHour(hour)
        DailyNotifRepo.setDailyNotifMinute(minute)
        postUpdate()
    }
}