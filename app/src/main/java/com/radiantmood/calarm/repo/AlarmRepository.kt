package com.radiantmood.calarm.repo

import androidx.annotation.WorkerThread
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class AlarmRepository {

    private val dao = database.alarmDao()

    @WorkerThread
    suspend fun queryAlarms(): List<UserAlarm> = withContext(Dispatchers.Default) {
        dao.getAll()
    }

    @WorkerThread
    suspend fun getForEvent(eventId: Int) = withContext(Dispatchers.Default) {
        dao.findByEventId(eventId)
    }

    @WorkerThread
    suspend fun add(alarm: UserAlarm) = withContext(Dispatchers.Default) {
        dao.insertAll(alarm)
    }

    @WorkerThread
    suspend fun remove(eventId: Int) = withContext(Dispatchers.Default) {
        getForEvent(eventId)?.let { dao.delete(it) }
    }
}

@Entity
data class UserAlarm(
    @PrimaryKey val eventId: Int,
    val calendar: Calendar,
    val title: String
)

@Dao
interface AlarmDao {
    @Query("SELECT * FROM UserAlarm")
    suspend fun getAll(): List<UserAlarm>

    @Query("SELECT * FROM UserAlarm WHERE eventId LIKE :id LIMIT 1")
    suspend fun findByEventId(id: Int): UserAlarm?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg alarms: UserAlarm)

    @Delete
    suspend fun delete(alarm: UserAlarm)
}

