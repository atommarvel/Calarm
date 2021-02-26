package com.radiantmood.calarm.screen.settings

import com.radiantmood.calarm.screen.FinishedModelContainer
import com.radiantmood.calarm.screen.calendars.CalendarSelectionModel

data class SettingsScreenModel(val selectedCalendars: List<CalendarSelectionModel>) : FinishedModelContainer<SettingsScreenModel>()

