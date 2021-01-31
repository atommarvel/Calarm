package com.radiantmood.calarm.util

import java.text.SimpleDateFormat
import java.util.*

private const val timePattern = "hh:mma"
private fun getTimeFormat() = SimpleDateFormat(timePattern, Locale.getDefault())

fun Calendar.formatTime() = getTimeFormat().format(time).orEmpty()