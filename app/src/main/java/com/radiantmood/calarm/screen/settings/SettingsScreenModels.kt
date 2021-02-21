package com.radiantmood.calarm.screen.settings

import com.radiantmood.calarm.screen.calendars.CalendarSelectionModel

data class SettingsScreenModel(val selectedCalendars: List<CalendarSelectionModel>) {
    companion object {
        fun getEmpty() = SettingsScreenModel(emptyList())
    }
}

