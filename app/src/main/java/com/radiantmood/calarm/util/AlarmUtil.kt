package com.radiantmood.calarm.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.radiantmood.calarm.AlarmExperienceActivity
import com.radiantmood.calarm.calarm
import java.util.*


class AlarmUtil {

    private val am: AlarmManager by lazy { calarm.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun scheduleAlarm(calendar: Calendar) {
        val intent = Intent(calarm, AlarmExperienceActivity::class.java)
        intent.putExtra("isAlarm", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pIntentFlags = Intent.FLAG_ACTIVITY_NEW_TASK or PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pIntent = PendingIntent.getActivity(calarm, 101, intent, pIntentFlags)
        val info = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pIntent)
        am.setAlarmClock(info, pIntent)
        Toast.makeText(calarm, "Alarm set for ${calendar.formatTime()}", Toast.LENGTH_SHORT).show()
    }
}