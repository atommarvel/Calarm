package com.radiantmood.calarm.repo

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import com.radiantmood.calarm.calarm
import java.util.*

class AlarmRepository {

    // TODO: adopt Room
    private val eventIdsToAlarms = mutableMapOf<Int, UserAlarm>()
    private val prefs = calarm.getSharedPreferences("ALARMS_REPO", Context.MODE_PRIVATE)
    private val key = "ALARM_"
    private val keyCal = key + "CAL_"
    private val keyIds = key + "IDS"

    init {
        prefs.getStringSet(keyIds, mutableSetOf())?.map { it.toInt() }?.forEach { id ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = prefs.getLong(keyCal + id, 0)
            }
            eventIdsToAlarms[id] = UserAlarm(id, calendar)
        }
    }

    @WorkerThread
    suspend fun queryAlarms(): List<UserAlarm> = eventIdsToAlarms.values.toList()

    @WorkerThread
    suspend fun getForEvent(eventId: Int) = eventIdsToAlarms[eventId]

    @WorkerThread
    suspend fun add(alarm: UserAlarm) {
        eventIdsToAlarms[alarm.eventId] = alarm
        updatePrefs()
    }

    @WorkerThread
    suspend fun remove(eventId: Int) {
        eventIdsToAlarms.remove(eventId)
        updatePrefs()
    }

    private fun updatePrefs() = prefs.edit {
        clear()
        val ids = mutableSetOf<String>()
        eventIdsToAlarms.forEach { (id, alarm) ->
            ids.add(id.toString())
            putLong(keyCal + id, alarm.calendar.timeInMillis)
        }
        putStringSet(keyIds, ids)
    }

    data class UserAlarm(val eventId: Int, val calendar: Calendar)
}