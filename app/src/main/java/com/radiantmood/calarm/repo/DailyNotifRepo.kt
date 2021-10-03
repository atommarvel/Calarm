package com.radiantmood.calarm.repo

import com.radiantmood.calarm.util.CalendarAt
import java.util.*

object DailyNotifRepo {

    fun isDailyNotifEnabled(): Boolean = PrefsRepo.isDailyNotifEnabled ?: false

    fun toggleIsDailyNotifEnabled() {
        PrefsRepo.isDailyNotifEnabled = PrefsRepo.isDailyNotifEnabled != true
    }

    fun updateScheduledNotification() {

    }

    fun getDailyNotifTime(): Calendar? =
        if (isDailyNotifEnabled()) {
            val hour = PrefsRepo.dailyNotifHour ?: 9L
            val minute = PrefsRepo.dailyNotifMinute ?: 0L
            CalendarAt(hour.toInt(), minute.toInt())
        } else null

    fun setDailyNotifHour(hour: Int) {
        PrefsRepo.dailyNotifHour = hour.toLong()
    }

    fun setDailyNotifMinute(minute: Int) {
        PrefsRepo.dailyNotifMinute = minute.toLong()
    }

}