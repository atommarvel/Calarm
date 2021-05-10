package com.radiantmood.calarm.screen.settings

import com.radiantmood.calarm.screen.FinishedModelContainer
import com.radiantmood.calarm.screen.calendars.CalendarSelectionModel
import java.util.*

data class SettingsScreenModel(
    val selectedCalendars: List<CalendarSelectionModel>,
    val dailyNotifTime: Calendar?,
) : FinishedModelContainer<SettingsScreenModel>()

