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
    suspend fun getForEvent(eventId: Int, eventPart: EventPart) = withContext(Dispatchers.Default) {
        dao.findByEventId(eventId, eventPart)
    }

    @WorkerThread
    suspend fun getAllForEvent(eventId: Int): List<UserAlarm> = withContext(Dispatchers.Default) {
        dao.findAllByEventId(eventId)
    }

    @WorkerThread
    suspend fun add(alarm: UserAlarm) = withContext(Dispatchers.Default) {
        dao.insertAll(alarm)
    }

    @WorkerThread
    suspend fun remove(eventId: Int, eventPart: EventPart) = withContext(Dispatchers.Default) {
        getForEvent(eventId, eventPart)?.let { dao.delete(it) }
    }
}

enum class EventPart {
    START, END;

    fun getTargetCal(calEvent: EventRepository.CalEvent) = when (this) {
        START -> calEvent.start
        END -> calEvent.end
    }

    operator fun plus(eventId: Int): String = eventId.toString() + this.name
}

@Entity
data class UserAlarm(
    @PrimaryKey val alarmId: String,
    val eventId: Int,
    val calendar: Calendar,
    val title: String,
    val eventPart: EventPart
)

@Dao
interface AlarmDao {
    @Query("SELECT * FROM UserAlarm")
    suspend fun getAll(): List<UserAlarm>

    @Query("SELECT * FROM UserAlarm WHERE eventId LIKE :id AND eventPart like :part LIMIT 1")
    suspend fun findByEventId(id: Int, part: EventPart): UserAlarm?

    @Query("SELECT * FROM UserAlarm WHERE eventId LIKE :id")
    suspend fun findAllByEventId(id: Int): List<UserAlarm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg alarms: UserAlarm)

    @Delete
    suspend fun delete(alarm: UserAlarm)
}

