package com.radiantmood.calarm.screen.settings

import com.radiantmood.calarm.screen.FinishedUiStateContainer
import com.radiantmood.calarm.screen.calendars.CalendarSelectionUiState
import java.util.*

data class SettingsScreenUiState(
    val selectedCalendars: List<CalendarSelectionUiState>,
    val dailyNotifTime: Calendar?,
) : FinishedUiStateContainer<SettingsScreenUiState>()

