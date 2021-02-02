package com.radiantmood.calarm.repo

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract.Calendars
import androidx.annotation.WorkerThread
import com.radiantmood.calarm.calarm


class CalendarRepository {

    @WorkerThread
    suspend fun queryCalendars(): List<UserCal> = CalendarCursor().map { it }

    class CalendarCursor : Iterable<UserCal> {

        val cursor: Cursor

        // TODO: get calendar color
        private val projection = arrayOf(Calendars._ID, Calendars.CALENDAR_DISPLAY_NAME)

        init {
            val builder: Uri.Builder = Calendars.CONTENT_URI.buildUpon()
            val contentResolver: ContentResolver = calarm.contentResolver
            cursor = checkNotNull(contentResolver.query(builder.build(), projection, null, null, null))
        }

        override fun iterator(): Iterator<UserCal> = object : Iterator<UserCal> {
            var index = 0

            override fun hasNext(): Boolean = (index < cursor.count).also { if (!it) cursor.close() }

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
}