package com.radiantmood.calarm.repo

import android.content.Context
import androidx.core.content.edit
import com.radiantmood.calarm.calarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SelectedCalendarsRepository {

    private val ids: MutableSet<Int>
    private val prefs = calarm.getSharedPreferences("SELECTED_CALENDARS_REPO", Context.MODE_PRIVATE)
    private val key = "SELECTED_IDS"

    init {
        ids = prefs.getString(key, "")?.split(",")?.mapNotNull { it.toIntOrNull() }?.toMutableSet() ?: mutableSetOf()
    }

    suspend fun add(calendarId: Int) = withContext(Dispatchers.Default) {
        ids.add(calendarId)
        updatePrefs()
    }

    suspend fun remove(calendarId: Int) = withContext(Dispatchers.Default) {
        ids.remove(calendarId)
        updatePrefs()
    }

    suspend fun getAll(): List<Int> = withContext(Dispatchers.Default) {
        ids.toList()
    }

    private fun updatePrefs() = prefs.edit {
        putString(key, ids.joinToString())
    }
}