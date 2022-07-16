package com.radiantmood.calarm.repo

import android.content.ContentUris
import android.net.Uri
import android.provider.CalendarContract.Instances.*
import androidx.annotation.WorkerThread
import com.radiantmood.calarm.repo.CursorValueType.*
import com.radiantmood.calarm.util.CalendarAtTime
import com.radiantmood.calarm.util.atEndOfDay
import com.radiantmood.calarm.util.atStartOfDay
import java.util.*
import java.util.concurrent.TimeUnit

class EventRepository {

    data class CalEvent(val calId: Int, val calName: String, val eventId: Int, val title: String, val start: Calendar, val end: Calendar, val calColorInt: Int)

    @WorkerThread
    fun queryEvents(calIds: List<Int> = emptyList()): List<CalEvent> {
        val today = Calendar.getInstance().atStartOfDay()
        return EventCursorIterable(calIds = calIds)
            .map { it }
            .filter { !it.start.before(today) }
            .sortedBy { it.start }
    }

    @WorkerThread
    fun queryTomorrowsEvents(calIds: List<Int> = emptyList()): List<CalEvent> {
        val tmoMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)
        val tmo = CalendarAtTime(tmoMillis).atStartOfDay()
        return EventCursorIterable(
            startTime = tmo,
            endTime = CalendarAtTime(tmoMillis).atEndOfDay(),
            calIds = calIds
        ).map { it }
            .filter { !it.start.before(tmo) }
            .sortedBy { it.start }
    }

    class EventCursorIterable(
        private val startTime: Calendar = CalendarAtTime(System.currentTimeMillis()),
        private val endTime: Calendar = CalendarAtTime(System.currentTimeMillis()).atEndOfDay(),
        private val calIds: List<Int> = emptyList()
    ) : CursorIterable<CalEvent>() {

        val calId = CALENDAR_ID via INT
        val calName = CALENDAR_DISPLAY_NAME via STRING
        val title = TITLE via STRING
        val begin = BEGIN via LONG
        val end = END via LONG
        val eventId = EVENT_ID via INT
        val color = CALENDAR_COLOR via INT

        override val outputFields: List<OutputField> = listOf(calId, calName, title, begin, end, eventId, color)

        override fun assemble(): CalEvent {
            val calStart = CalendarAtTime(this[begin])
            val calEnd = CalendarAtTime(this[end])
            return CalEvent(
                calId = this[calId],
                calName = this[calName],
                eventId = this[eventId],
                title = this[title],
                start = calStart,
                end = calEnd,
                calColorInt = this[color]
            )
        }

        /**
         * https://github.com/mtrung/android-WatchFace/blob/feature/wearable_calendar/Wearable/src/main/java/com/example/android/wearable/watchface/calendar/CalendarEvent.java
         *
         * https://stackoverflow.com/questions/26844770/how-to-get-access-to-the-calendars-on-a-android-phone
         * https://github.com/CyanogenMod/android_packages_apps_Calendar/blob/cm-12.0/src/com/android/calendar/Event.java#L307
         */
        override fun buildContentUri(): Uri {
            val builder: Uri.Builder = CONTENT_URI.buildUpon()
            ContentUris.appendId(builder, startTime.timeInMillis)
            ContentUris.appendId(builder, endTime.timeInMillis)
            return builder.build()
        }

        override fun buildSelection(): String = buildString {
            if (calIds.isNotEmpty()) {
                append("(")
                calIds.forEachIndexed { index, id ->
                    append("($CALENDAR_ID = $id)")
                    if (index != calIds.lastIndex) {
                        append(" OR ")
                    }
                }
                append(")")
            }
        }
    }
}