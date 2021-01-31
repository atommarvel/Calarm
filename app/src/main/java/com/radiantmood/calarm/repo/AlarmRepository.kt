package com.radiantmood.calarm.repo

import androidx.annotation.WorkerThread
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmRepository {

    @WorkerThread
    suspend fun queryAlarms(): List<UserAlarm> = listOf(getMinsDebugAlarm())

    fun getMinsDebugAlarm(future: Long = 1) = UserAlarm(Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(future)
    })

    fun getSecondsDebugAlarm(future: Long = 20) = UserAlarm(Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(future)
    })

    data class UserAlarm(val calendar: Calendar)
}