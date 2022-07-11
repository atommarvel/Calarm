package com.radiantmood.calarm.repo

import androidx.room.*
import com.radiantmood.calarm.calarm
import java.util.*

@Database(entities = [SelectedCal::class, UserAlarm::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selectedCalDao(): SelectedCalDao
    abstract fun alarmDao(): AlarmDao
}

val database by lazy { Room.databaseBuilder(calarm, AppDatabase::class.java, "calarm-db").build() }

class Converters {
    @TypeConverter
    fun fromMilliTime(time: Long?): Calendar? = time?.let {
        Calendar.getInstance().apply {
            timeInMillis = time
        }
    }

    @TypeConverter
    fun calToMilliTime(calendar: Calendar?): Long? = calendar?.timeInMillis
}