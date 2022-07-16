package com.radiantmood.calarm.screen.calendars

import androidx.compose.ui.graphics.Color
import com.radiantmood.calarm.screen.FinishedUiStateContainer

data class CalendarSelectionUiState(val name: String, val isSelected: Boolean, val color: Color, val onCalendarToggled: () -> Unit)

data class CalendarsSelectionScreenUiState(val calendarSelectionUiStates: List<CalendarSelectionUiState>) :
    FinishedUiStateContainer<CalendarsSelectionScreenUiState>()