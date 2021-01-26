package com.radiantmood.calarm

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract.Calendars
import androidx.annotation.WorkerThread


class CalendarRepository {

    @WorkerThread
    suspend fun queryCalendars(): List<UserCal> = CalendarCursor().map { it }

    class CalendarCursor: Iterable<UserCal> {

        val cursor: Cursor
        private val CAL_PROJECTION = arrayOf(Calendars._ID, Calendars.CALENDAR_DISPLAY_NAME)

        init {
            val builder: Uri.Builder = Calendars.CONTENT_URI.buildUpon()
            val contentResolver: ContentResolver = calarm.contentResolver
            cursor = checkNotNull(contentResolver.query(builder.build(), CAL_PROJECTION, null, null, null))
        }

        override fun iterator(): Iterator<UserCal> = object : Iterator<UserCal> {
            var index = 0

            override fun hasNext(): Boolean = index < cursor.count

            override fun next(): UserCal = UserCal.fromCursor(cursor, index).also { index++ }

        }
    }

    data class UserCal(val id: Int, val name: String) {
        companion object {
            fun fromCursor(cursor: Cursor, position: Int): UserCal {
                cursor.moveToPosition(position)
                return UserCal(cursor.getInt(0), cursor.getString(1))
            }
        }
    }

    class EventCursor {

        private val EVENT_PROJECTION: Array<String> = arrayOf(
            Calendars._ID,                     // 0
            Calendars.ACCOUNT_NAME,            // 1
            Calendars.CALENDAR_DISPLAY_NAME,   // 2
            Calendars.OWNER_ACCOUNT            // 3
        )

    }
}