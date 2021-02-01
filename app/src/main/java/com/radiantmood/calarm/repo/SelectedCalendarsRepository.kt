package com.radiantmood.calarm.repo

import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SelectedCalendarsRepository {

    private val dao = database.selectedCalDao()

    suspend fun add(calendarId: Int) = withContext(Dispatchers.Default) {
        dao.insertAll(SelectedCal(calendarId))
    }

    suspend fun remove(calendarId: Int) = withContext(Dispatchers.Default) {
        dao.delete(SelectedCal(calendarId))
    }

    suspend fun getAll(): List<Int> = withContext(Dispatchers.Default) {
        dao.getAll().map { it.id }
    }
}

@Entity
data class SelectedCal(
    @PrimaryKey val id: Int
)

@Dao
interface SelectedCalDao {
    @Query("SELECT * FROM SelectedCal")
    suspend fun getAll(): List<SelectedCal>

    @Insert
    suspend fun insertAll(vararg selectedCals: SelectedCal)

    @Delete
    suspend fun delete(selectedCal: SelectedCal)
}