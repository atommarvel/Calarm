package com.radiantmood.calarm.screen.calendars

import com.radiantmood.calarm.screen.LoadingState
import com.radiantmood.calarm.screen.ModelState

data class CalendarSelectionModel(val name: String, val isSelected: Boolean, val onCalendarToggled: () -> Unit)

data class CalendarScreenModel(val state: ModelState, val calendarSelectionModels: List<CalendarSelectionModel>) {
    companion object {
        fun getEmpty() = CalendarScreenModel(LoadingState, emptyList())
    }
}