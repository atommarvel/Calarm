package com.radiantmood.calarm

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract.Calendars
import android.provider.CalendarContract.Events.*
import android.text.format.DateUtils
import androidx.annotation.WorkerThread
import java.util.*


class CalendarRepository {

    @WorkerThread
    suspend fun queryCalendars(): List<UserCal> = CalendarCursor().map { it }

    @WorkerThread
    suspend fun queryEvents(): List<CalEvent> = EventCursor().map { it }

    class CalendarCursor : Iterable<UserCal> {

        val cursor: Cursor
        private val CAL_PROJECTION = arrayOf(Calendars._ID, Calendars.CALENDAR_DISPLAY_NAME)

        init {
            val builder: Uri.Builder = Calendars.CONTENT_URI.buildUpon()
            val contentResolver: ContentResolver = calarm.contentResolver
            cursor = checkNotNull(contentResolver.query(builder.build(), CAL_PROJECTION, null, null, null))
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

    class EventCursor: Iterable<CalEvent> {

        val cursor: Cursor
        private val EVENT_PROJECTION: Array<String> = arrayOf(TITLE, DTSTART)

        init {
            val builder: Uri.Builder = CONTENT_URI.buildUpon()

            val start = System.currentTimeMillis()
            val end = start + DateUtils.DAY_IN_MILLIS
            // TODO: only query for selected calendars
            val contentResolver: ContentResolver = calarm.contentResolver
            val selection = "(( $DTSTART >= $start ) AND ( $DTSTART <= $end ))"
            cursor = checkNotNull(contentResolver.query(builder.build(), EVENT_PROJECTION, selection, null, null))
        }

        override fun iterator(): Iterator<CalEvent> = object : Iterator<CalEvent> {
            var index = 0

            override fun hasNext(): Boolean = (index < cursor.count).also { if (!it) cursor.close() }

            override fun next(): CalEvent = CalEvent.fromCursor(cursor, index).also { index++ }

        }

    }

    data class CalEvent(val title: String, val date: Calendar) {
        companion object {
            fun fromCursor(cursor: Cursor, position: Int): CalEvent {
                cursor.moveToPosition(position)
                val date = Calendar.getInstance().apply {
                    timeInMillis = cursor.getLong(1)
                }
                return CalEvent(cursor.getString(0), date)
            }
        }
    }

    data class UserAlarm(val date: Calendar)
}