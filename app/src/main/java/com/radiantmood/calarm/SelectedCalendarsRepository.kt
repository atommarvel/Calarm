package com.radiantmood.calarm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SelectedCalendarsRepository {

    private val ids = mutableSetOf<Int>()

    suspend fun add(calendarId: Int) = withContext(Dispatchers.Default) {
        ids.add(calendarId)
    }

    suspend fun remove(calendarId: Int) = withContext(Dispatchers.Default) {
        ids.remove(calendarId)
    }

    suspend fun getAll(): List<Int> = withContext(Dispatchers.Default) {
        ids.toList()
    }
}