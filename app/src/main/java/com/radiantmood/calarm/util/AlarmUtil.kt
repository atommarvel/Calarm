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

    fun scheduleAlarm(calendar: Calendar, eventId: Int) {
        val pIntent = createPendingIntent(eventId)
        val info = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pIntent)
        am.setAlarmClock(info, pIntent)
        Toast.makeText(calarm, "Alarm set for ${calendar.formatTime()}", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(eventId: Int) {
        val pIntent = createPendingIntent(eventId)
        am.cancel(pIntent)
    }

    private fun createPendingIntent(eventId: Int): PendingIntent {
        val intent = Intent(calarm, AlarmExperienceActivity::class.java).apply {
            putExtra("isAlarm", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pIntentFlags = Intent.FLAG_ACTIVITY_NEW_TASK or PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(calarm, eventId, intent, pIntentFlags)
    }

    // TODO: a simple way to cancel all scheduled alarms registered to AlarmManager

    // TODO: reconcile all alarms to their events to make sure there are no unmatched alarms due to event changes
    // is there a way to listen for updates in calendar events? sync adapters?
    // TODO: delete alarms when they go off
}