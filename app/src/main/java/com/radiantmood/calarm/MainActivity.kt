package com.radiantmood.calarm

import android.Manifest.permission.READ_CALENDAR
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.radiantmood.calarm.CalendarRepository.UserCal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val permissions by lazy { PermissionsKit(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            renderScreen()
        }
    }

    private fun renderScreen() {
        // TODO: move to an "app" object?
        if (permissions.isCalendarPermissionGranted) {
            renderAlarms()
        } else {
            renderPermissionRequired()
        }
    }

    private fun fetchAndRenderCalendars() = lifecycleScope.launch {
        val calRepo = CalendarRepository()
        val calendars = withContext(Dispatchers.Default) { calRepo.queryCalendars() }
        renderCalendars(calendars)
    }

    private fun renderCalendars(calendars: List<UserCal>) = render {
        // TODO: backstack mechanism
        CalendarsScreen(calendars) {
            renderScreen()
        }
    }

    private fun renderAlarms() = render {
        AlarmsScreen {
            fetchAndRenderCalendars()
        }
    }

    private fun renderPermissionRequired() = render { PermissionScreen { getCalendarPermission() } }

    private fun getCalendarPermission() = lifecycleScope.launch {
        permissions.requestPermission(READ_CALENDAR)
        renderScreen()
    }
}