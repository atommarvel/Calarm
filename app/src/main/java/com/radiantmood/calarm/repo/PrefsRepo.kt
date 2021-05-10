package com.radiantmood.calarm.repo

import android.content.Context
import com.radiantmood.calarm.calarm
import com.radiantmood.calarm.prefs.getPrefDelegate

object PrefsRepo {

    val prefs by lazy { calarm.getSharedPreferences("calarm.prefs", Context.MODE_PRIVATE) }

    var isDailyNotifEnabled: Boolean? by getPrefDelegate(prefs, "isDailyNotifEnabled", false)
    var dailyNotifHour: Long? by getPrefDelegate(prefs, "dailyAlarmHour", null)
    var dailyNotifMinute: Long? by getPrefDelegate(prefs, "dailyAlarmMinute", null)
}