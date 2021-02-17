package com.radiantmood.calarm.repo

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract.Instances.*
import androidx.annotation.WorkerThread
import com.radiantmood.calarm.calarm
import com.radiantmood.calarm.repo.CursorValueType.*
import com.radiantmood.calarm.util.CalendarAtTime
import com.radiantmood.calarm.util.atEndOfDay
import com.radiantmood.calarm.util.atStartOfDay
import java.util.*
import java.util.concurrent.TimeUnit

class EventRepository {

    @WorkerThread
    suspend fun queryEvents(): List<CalEvent> = EventCursor().map { it }.sortedBy { it.start }

    @WorkerThread
    suspend fun queryTomorrowsEvents(): List<CalEvent> {
        val tmoMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)
        return EventCursor(
            startTime = CalendarAtTime(tmoMillis).atStartOfDay(),
            endTime = CalendarAtTime(tmoMillis).atEndOfDay()
        ).map { it }.sortedBy { it.start }
    }

    class EventCursor(
        val startTime: Calendar = CalendarAtTime(System.currentTimeMillis()),
        val endTime: Calendar = CalendarAtTime(System.currentTimeMillis()).atEndOfDay()
    ) : CursorHelper<CalEvent>() {

        val calId = CALENDAR_ID via INT
        val title = TITLE via STRING
        val begin = BEGIN via LONG
        val end = END via LONG
        val eventId = EVENT_ID via INT

        override val projections: List<Projection> = listOf(calId, title, begin, end, eventId)

        override fun assemble(cursor: Cursor): CalEvent {
            val calStart = CalendarAtTime(this[begin])
            val calEnd = CalendarAtTime(this[end])
            return CalEvent(
                calId = this[calId],
                eventId = this[eventId],
                title = this[title],
                start = calStart,
                end = calEnd
            )
        }

        /**
         * https://github.com/mtrung/android-WatchFace/blob/feature/wearable_calendar/Wearable/src/main/java/com/example/android/wearable/watchface/calendar/CalendarEvent.java
         *
         * https://stackoverflow.com/questions/26844770/how-to-get-access-to-the-calendars-on-a-android-phone
         * https://github.com/CyanogenMod/android_packages_apps_Calendar/blob/cm-12.0/src/com/android/calendar/Event.java#L307
         */
        override val cursor: Cursor by lazy {
            val builder: Uri.Builder = CONTENT_URI.buildUpon()
            ContentUris.appendId(builder, startTime.timeInMillis)
            ContentUris.appendId(builder, endTime.timeInMillis)
            // TODO: only query for selected calendars
            val contentResolver: ContentResolver = calarm.contentResolver
            // TODO: sort order
            checkNotNull(contentResolver.query(builder.build(), keys, null, null, null))
        }

    }

    data class CalEvent(val calId: Int, val eventId: Int, val title: String, val start: Calendar, val end: Calendar)
}