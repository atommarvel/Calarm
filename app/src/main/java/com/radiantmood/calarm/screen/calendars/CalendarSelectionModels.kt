package com.radiantmood.calarm.screen.calendars

import androidx.compose.ui.graphics.Color
import com.radiantmood.calarm.screen.FinishedModelContainer

data class CalendarSelectionModel(val name: String, val isSelected: Boolean, val color: Color, val onCalendarToggled: () -> Unit)

data class CalendarsSelectionScreenModel(val calendarSelectionModels: List<CalendarSelectionModel>) : FinishedModelContainer<CalendarsSelectionScreenModel>()