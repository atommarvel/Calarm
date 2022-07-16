package com.radiantmood.calarm.common

import android.graphics.Color
import com.radiantmood.calarm.repo.EventPart
import com.radiantmood.calarm.repo.EventRepository.CalEvent
import com.radiantmood.calarm.repo.UserAlarm
import java.util.*

/**
 * Helpers for debugging.
 */

fun getDebugEvent(start: Calendar = getLateNightCalendar()): CalEvent {
    val end = getFutureCalendar(initialCalendar = start, minutesInFuture = 31)
    return CalEvent(-1, "Debug Calendar", -1, "Debug Event", start, end, Color.RED)
}

fun getDebugAlarm(calEvent: CalEvent = getDebugEvent()): UserAlarm =
    UserAlarm(EventPart.START + calEvent.eventId, calEvent.eventId, calEvent.start, calEvent.title, EventPart.START, 0)

val DoNothingLambda = { /*Do Nothing*/ }