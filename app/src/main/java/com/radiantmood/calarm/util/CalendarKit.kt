package com.radiantmood.calarm.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val timePattern = "h:mma"
private fun getTimeFormat() = SimpleDateFormat(timePattern, Locale.getDefault())

fun CalendarAtTime(timeInMilliseconds: Long) = Calendar.getInstance().apply { timeInMillis = timeInMilliseconds }

fun Calendar.formatTime() = getTimeFormat().format(time).orEmpty()

fun getFutureCalendar(initialCalendar: Calendar? = null, secondsInFuture: Long = 0, minutesInFuture: Long = 0) = Calendar.getInstance().apply {
    val initial = initialCalendar?.timeInMillis ?: System.currentTimeMillis()
    timeInMillis = initial + TimeUnit.SECONDS.toMillis(secondsInFuture) + TimeUnit.MINUTES.toMillis(minutesInFuture)
}

fun getLateNightCalendar() = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.MINUTE, 59)
}