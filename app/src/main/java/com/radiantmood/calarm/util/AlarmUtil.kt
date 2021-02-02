package com.radiantmood.calarm.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.radiantmood.calarm.AlarmExperienceActivity
import com.radiantmood.calarm.calarm
import com.radiantmood.calarm.repo.UserAlarm
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit


class AlarmUtil {

    private val am: AlarmManager by lazy { calarm.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun scheduleAlarm(userAlarm: UserAlarm) {
        val time = Calendar.getInstance().apply {
            timeInMillis = userAlarm.calendar.timeInMillis + TimeUnit.MINUTES.toMillis(userAlarm.offsetMin.toLong())
        }
        val pIntent = createPendingIntent(userAlarm)
        val info = AlarmManager.AlarmClockInfo(time.timeInMillis, pIntent)
        am.setAlarmClock(info, pIntent)
        Toast.makeText(calarm, "Alarm set for ${time.formatTime()}", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(userAlarm: UserAlarm) {
        val pIntent = createPendingIntent(userAlarm)
        am.cancel(pIntent)
    }

    private fun createPendingIntent(userAlarm: UserAlarm): PendingIntent {
        val intent = Intent(calarm, AlarmExperienceActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        AlarmIntentData.fromUserAlarm(userAlarm).putInIntent(intent)
        val pIntentFlags = Intent.FLAG_ACTIVITY_NEW_TASK or PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(calarm, userAlarm.eventId, intent, pIntentFlags)
    }

    data class AlarmIntentData(val title: String, val eventId: Int) : Serializable {

        fun putInIntent(intent: Intent) = intent.putExtra(key, this)

        companion object {
            private const val key = "ALARM_INTENT_DATA"
            fun fromIntent(intent: Intent): AlarmIntentData? = intent.getSerializableExtra(key) as? AlarmIntentData
            fun fromUserAlarm(userAlarm: UserAlarm) = AlarmIntentData(userAlarm.title, userAlarm.eventId)
        }
    }

    // TODO: a simple way to cancel all scheduled alarms registered to AlarmManager

    // TODO: reconcile all alarms to their events to make sure there are no unmatched alarms due to event changes
    // is there a way to listen for updates in calendar events? sync adapters?
    // TODO: delete alarms when they go off
}