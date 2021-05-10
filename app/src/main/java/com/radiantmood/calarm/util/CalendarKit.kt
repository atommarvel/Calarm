package com.radiantmood.calarm.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val timePattern = "h:mma"
private fun getTimeFormat() = SimpleDateFormat(timePattern, Locale.getDefault())

fun CalendarAtTime(timeInMilliseconds: Long) = Calendar.getInstance().apply { timeInMillis = timeInMilliseconds }

fun CalendarAt(hour: Int, minute: Int) = Calendar.getInstance().atStartOfDay().apply {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
}

fun Calendar.atStartOfDay() = apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    set(Calendar.HOUR, 0)
    set(Calendar.AM_PM, 0)
}

fun Calendar.atEndOfDay() = apply {
    set(Calendar.MILLISECOND, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MINUTE, 59)
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.HOUR, 11)
    set(Calendar.AM_PM, 1)
}

fun Calendar.formatTime() = getTimeFormat().format(time).toLowerCase(Locale.getDefault())

fun getFutureCalendar(initialCalendar: Calendar? = null, secondsInFuture: Long = 0, minutesInFuture: Long = 0) = Calendar.getInstance().apply {
    val initial = initialCalendar?.timeInMillis ?: System.currentTimeMillis()
    timeInMillis = initial + TimeUnit.SECONDS.toMillis(secondsInFuture) + TimeUnit.MINUTES.toMillis(minutesInFuture)
}

fun getLateNightCalendar() = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.MINUTE, 59)
}

fun getHour(timeInMilliseconds: Long): Int = CalendarAtTime(timeInMilliseconds).get(Calendar.HOUR_OF_DAY)