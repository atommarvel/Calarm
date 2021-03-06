package com.radiantmood.calarm.util

import android.graphics.Color
import com.radiantmood.calarm.repo.EventRepository.CalEvent
import com.radiantmood.calarm.repo.UserAlarm
import java.util.*

fun getDebugEvent(start: Calendar = getLateNightCalendar()): CalEvent {
    val end = getFutureCalendar(initialCalendar = start, minutesInFuture = 31)
    return CalEvent(-1, "Debug Calendar", -1, "Debug Event", start, end, Color.RED)
}

fun getDebugAlarm(calEvent: CalEvent = getDebugEvent()): UserAlarm = UserAlarm(calEvent.eventId, calEvent.start, calEvent.title)