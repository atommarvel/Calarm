package com.radiantmood.calarm.repo

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import androidx.annotation.WorkerThread
import com.radiantmood.calarm.calarm
import java.util.*

class EventRepository {

    @WorkerThread
    suspend fun queryEvents(): List<CalEvent> = EventCursor().map { it }.sortedBy { it.start }

    /**
     * https://stackoverflow.com/questions/26844770/how-to-get-access-to-the-calendars-on-a-android-phone
     * https://github.com/CyanogenMod/android_packages_apps_Calendar/blob/cm-12.0/src/com/android/calendar/Event.java#L307
     */
    class EventCursor : Iterable<CalEvent> {

        val cursor: Cursor
        private val projection: Array<String> = arrayOf(
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events._ID
        )

        init {
            val builder: Uri.Builder = CalendarContract.Events.CONTENT_URI.buildUpon()

            val start = System.currentTimeMillis()
            val end = Calendar.getInstance().apply {
                timeInMillis = start
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                set(Calendar.HOUR, 11)
                set(Calendar.AM_PM, 1)
            }.timeInMillis
            // TODO: only query for selected calendars
            val contentResolver: ContentResolver = calarm.contentResolver
            val selection = "(( ${CalendarContract.Events.DTSTART} >= $start ) AND ( ${CalendarContract.Events.DTSTART} <= $end ))"
            // TODO: sort order
            cursor = checkNotNull(contentResolver.query(builder.build(), projection, selection, null, null))
        }

        override fun iterator(): Iterator<CalEvent> = object : Iterator<CalEvent> {
            var index = 0

            override fun hasNext(): Boolean = (index < cursor.count).also { if (!it) cursor.close() }

            override fun next(): CalEvent = CalEvent.fromCursor(cursor, index).also { index++ }

        }

    }

    data class CalEvent(val calId: Int, val eventId: Int, val title: String, val start: Calendar, val end: Calendar) {
        companion object {
            fun fromCursor(cursor: Cursor, position: Int): CalEvent {
                cursor.moveToPosition(position)
                val start = Calendar.getInstance().apply {
                    timeInMillis = cursor.getLong(2)
                }
                val end = Calendar.getInstance().apply {
                    timeInMillis = cursor.getLong(3)
                }
                return CalEvent(cursor.getInt(0), cursor.getInt(4), cursor.getString(1).orEmpty(), start, end)
            }
        }
    }
}