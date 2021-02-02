package com.radiantmood.calarm.util

import com.radiantmood.calarm.repo.EventRepository.CalEvent
import com.radiantmood.calarm.repo.UserAlarm
import com.radiantmood.calarm.screen.EventDisplay
import java.util.*

fun getDebugEvent(start: Calendar = getLateNightCalendar()): CalEvent {
    val end = getFutureCalendar(initialCalendar = start, minutesInFuture = 31)
    return CalEvent(-1, -1, "Debug Event", start, end)
}

fun getDebugEventDisplay(alarm: UserAlarm? = null, createDebugAlarm: Boolean = false): EventDisplay {
    val debugEvent = getDebugEvent()
    return EventDisplay(debugEvent, if (alarm == null && createDebugAlarm) getDebugAlarm(debugEvent) else alarm)
}

fun getDebugAlarm(calEvent: CalEvent = getDebugEvent()): UserAlarm = UserAlarm(calEvent.eventId, calEvent.start, calEvent.title)