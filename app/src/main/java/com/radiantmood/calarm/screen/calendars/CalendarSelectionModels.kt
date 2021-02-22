package com.radiantmood.calarm.screen.calendars

import androidx.compose.ui.graphics.Color
import com.radiantmood.calarm.screen.LoadingState
import com.radiantmood.calarm.screen.ModelState

data class CalendarSelectionModel(val name: String, val isSelected: Boolean, val color: Color, val onCalendarToggled: () -> Unit)

data class CalendarsSelectionScreenModel(val state: ModelState, val calendarSelectionModels: List<CalendarSelectionModel>) {
    companion object {
        fun getEmpty() = CalendarsSelectionScreenModel(LoadingState, emptyList())
    }
}