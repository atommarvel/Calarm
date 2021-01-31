package com.radiantmood.calarm.repo

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract.Calendars
import android.provider.CalendarContract.Events.*
import android.text.format.DateUtils
import androidx.annotation.WorkerThread
import com.radiantmood.calarm.calarm
import java.util.*


class CalendarRepository {

    @WorkerThread
    suspend fun queryCalendars(): List<UserCal> = CalendarCursor().map { it }

    @WorkerThread
    suspend fun queryEvents(): List<CalEvent> = EventCursor().map { it }

    class CalendarCursor : Iterable<UserCal> {

        val cursor: Cursor
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

    /**
     * https://stackoverflow.com/questions/26844770/how-to-get-access-to-the-calendars-on-a-android-phone
     * https://github.com/CyanogenMod/android_packages_apps_Calendar/blob/cm-12.0/src/com/android/calendar/Event.java#L307
     */
    class EventCursor : Iterable<CalEvent> {

        val cursor: Cursor
        private val projection: Array<String> = arrayOf(CALENDAR_ID, TITLE, DTSTART, DTEND)

        init {
            val builder: Uri.Builder = CONTENT_URI.buildUpon()

            val start = System.currentTimeMillis()
            val end = start + DateUtils.DAY_IN_MILLIS
            // TODO: only query for selected calendars
            val contentResolver: ContentResolver = calarm.contentResolver
            val selection = "(( $DTSTART >= $start ) AND ( $DTSTART <= $end ))"
            // TODO: sort order
            cursor = checkNotNull(contentResolver.query(builder.build(), projection, selection, null, null))
        }

        override fun iterator(): Iterator<CalEvent> = object : Iterator<CalEvent> {
            var index = 0

            override fun hasNext(): Boolean = (index < cursor.count).also { if (!it) cursor.close() }

            override fun next(): CalEvent = CalEvent.fromCursor(cursor, index).also { index++ }

        }

    }

    data class CalEvent(val calId: Int, val title: String, val start: Calendar, val end: Calendar) {
        companion object {
            fun fromCursor(cursor: Cursor, position: Int): CalEvent {
                cursor.moveToPosition(position)
                val start = Calendar.getInstance().apply {
                    timeInMillis = cursor.getLong(2)
                }
                val end = Calendar.getInstance().apply {
                    timeInMillis = cursor.getLong(3)
                }
                return CalEvent(cursor.getInt(0), cursor.getString(1), start, end)
            }
        }
    }

    data class UserAlarm(val date: Calendar)
}